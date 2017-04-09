# Genetický algoritmus a Virtuálny stroj (Hľadač pokladov)
### Fakulta informatiky a informačných technológií
# Richard Mocák
# predmet : Umelá Inteligencia

## Zadanie 3B (krátky opis)

Majme hľadača pokladov, ktorý sa pohybuje vo svete definovanom dvojrozmernou mriežkou (viď. obrázok) a zbiera poklady, ktoré nájde po ceste. Začína na políčku označenom písmenom “S” a môže sa pohybovať štyrmi rôznymi smermi: “hore” H, “dolu” D, “doprava” P a “doľava” L. K dispozícii má konečný počet krokov. Jeho úlohou je nazbierať čo najviac pokladov. Za nájdenie pokladu sa považuje len pozícia, pri ktorej je hľadač aj poklad na tom istom políčku. Susedné políčka sa neberú do úvahy.


Tento špecifický spôsob evolučného programovania využíva spoločnú pamäť pre údaje a inštrukcie. Pamäť je na začiatku vynulovaná a naplnená od prvej bunky inštrukciami. Za programom alebo od určeného miesta sú uložené inicializačné údaje (ak sú nejaké potrebné). Po inicializácii sa začne vykonávať program od prvej pamäťovej bunky (Prvou je samozrejme bunka s adresou 000000). Inštrukcie modifikujú pamäťové bunky, môžu realizovať vetvenie, programové skoky, čítať nejaké údaje zo vstupu a prípadne aj zapisovať na výstup. Program sa končí inštrukciou na zastavenie, po stanovenom počte krokov, pri chybnej inštrukcii, po úplnom alebo nesprávnom výstupe. Kvalita programu sa ohodnotí na základe vyprodukovaného výstupu alebo, keď program nezapisuje na výstup, podľa výsledného stavu určených pamäťových buniek.

Celé zadanie sa nachádza [TU.](http://www2.fiit.stuba.sk/~kapustik/poklad.html)


## Riešenie úlohy
Aby som našiel riešenie tohto zadania s pomocou Genetického algoritmu, tak som musel zabezpečiť správnu reprezentáciu údajov zo zadania zadelenie operácií jednotlivým subjektom vysupujúcim v zadaní. V triede **Mapa** som inicializoval hashovaciu mapu, ktorá schovávala poklady, sledoval som hranice mapy, aby hľadač nehľadal poklad mimo mapy a informoval hľadača ak políčko na ktorom stál skrývalo poklad. V triede **HladacPokladov** som uchovával informácie o štartovacej pozícií, počte aktálne nájdených pokladov počas hľadania, počtu vykonaných krokov a aktuálnej pozicií na ktorej sa nachádza. Hľadač pokladov tiež vie vykonať pohyb podľa vstupu ("P" - vpravo, "L" - vľavo, ...). Ak sa ocitne mimo mapy, signalizuje výnimočný stav. **VirtualnyStroj** je trieda virtuálneho stroja, ktorý pracuje s Mapou, Hľadačom pokladov ale hlavne s **Jedincom**. Jedinec predstavuje program s inštrukciami zapísanými v požadovanom tvare v 64 pamäťových bunkách. Virtuálny stroj vie vykonať tieto operácie a odsledovať výstupné operácie jedinca a tak inštruovať hľadača ako sa posúvať po mape. Virtuálny stroj sa zastaví ak presiahne viac ako 500 inštrukcií, ak z mapy zistí že hľadač našiel už všetky poklady alebo ak sa hľadač ocitne vo výnimočnom stave mimo mapy. Ak sa zastaví, tak zapíše informácie o úspechoch hľadača do jedinca. Jedná sa o fitness hodnotu, počet nájdených pokladov a počet krokov. Teraz prichádza do hry **GenetickyAlgoritmus**, ktorý generuje nových jedincov do popúlacie, kotrú skúma na virtuálnoom stroji. Následne zapezpečí vygenerovanie lepšieho jedinca (jedincov) vďaka kríženiu a generácií. Genetický algoritmus sa zastaví ak našiel jedinca, ktorý našiel všetky poklady, alebo presiahne maximálny počet generácií. Pre zrozumiteľnosť som zmenil názvoslovie. Výraz jedinec predstavuje **chromozóm** a bunky prestavujú samotné **gény** ako v všeobecnom Genetickom algoritme.


## Algoritmus
Algoritmus uchováva viacero premenných, ktoré ovplyvňujú kroky algoritmu. Maximálny počet generácií, ktoré sa vykonajú kým sa algoritmus, nezastaví. Počet jedincov v populácií, podľa ktorého sa inicializuje pole Jedincov, ktoré predstavuje populáciu. Desatiné číslo, ktoré označuje pradepodobnosť mutácií a referencia na Mapu, Hladača pokladov, Virtuálny stroj. 

Teraz opíšem samotné kroky algoritmu.

### Inicializácia
Táto operácia prebehne počas behu algoritmu vždy iba raz. Pozostáva z 2 častí. Uložím **referenciu na objekty** Mapy, Hľadača a Virtuálny stroj s ktorými budem pracovať počas krokov algoritmu. Druhá časť predstavuje **vytvorenie prvej generácie** populácie. Pre počet jedincov sa do každého poľa populácia náhodne vygeneruje nový Jedinec. Inicialízaciu nájdeme v konštruktore triedy **GenetickyAlgoritmus**.

### Opakovanie krokov - slučka
Po inicializácií pokračujú kroky, ktoré sa opakujú pokiaľ sa algoritmus nezastaví. Hlavnou podmienkou je, že opakujeme kroky kým dosiahne algoritmus maximálny počet algoritmus. Algoritmus sa tiež zastaví, ak najúspešnejší jedinec v generácií našiel 5 pokladov. Kroky algoritmu, ktoré prebiehajú v slučke nájdeme v metóde **proces()** v triede **GenetickyAlgoritmus.**

### Ohodnotenie jedincov (fitness)
Genetický algoritmus pracuje s hodnotou úspešnosti **fitness** a vždy sa snaží nájsť riešenie s lokálne maximálnou fitness hodnotou. V mojej implementácií ohodnotíme každého jedinca po skončení behu programu jedinca na virtuálnom stroji. Po skončení sa zapíše do jedinca hodnota fitness, ktorá sa počíta z počtu pokladov a počtu krokov. Podľa návrhu v zadaní, som zjemnil výslednú hodnotu fitness o počet krokov, aby bolo lepšie ohodnotené riešenie ktoré nájde rovnaký počet pokladov s menším počtom krokov. Samotná formula vyzerá takto. 

fitness(poklady, kroky) = poklady * 1000 - kroky

Po ohodnotení si jedincov usporiadam podľa ich fitness hodnoty zostupne pomocou prioritného radu, ktorý ich usporiada v pomerne rýchlom čase O(n*log n). 

### Reprodukcia
Ďalším krokom v geneticom algoritme je reprodukcia novej generácie jedincov, od ktorých sa očakáva že budú mať priemerne vyššiu fitness hodnotu ako súčasná populácia. Aby sme toto dosiahli aj v mojom zadaní som využil viaceré techniky ako toto dosiahnuť.

#### Elitarizmus
Aby sme zabezpečili že sa nová populácia v ďalšej generácií výrazne nezhorší, tak posielam do novej generácie určité percento najúspešenejších jedincov zo súčasnej generácie. Nedochádza medzi nimi k žiadnemu kríženiu. Po menšom testovaní som zistil že 10% je vhodná hodnota.  


#### Metódy selekcie
Zo zvyšných 90% populácie vyberám po 2 jedincov, ktorý zabezpečia pomocou kríženia reprodukciu a vznik nového jedinca. Týchto dvoch  jedincov vyberáme pomocou 2 odišných metód selekcie. V každom prpade sa však snažíme vybrať jedincov, ktorý majú podľa hodnoty fitness najúspešenejšiu hodnotu reprodukovať v budúcej generácií jedinca, by mohol byť riešením algoritmu.

V algoritme využívam 2 metódy výberu jedincov:

**1. Ruleta**
Ruleta je predstavovaná polom jedincov. Každý jedinec sa nachádza v poli toľko krát, že pravdepobonosť výberu je priamo úmerná fitness hodnote úspešenosti. Počet som vypočítal tak, že som vyjadril v percentách fitness jedinca vzhľadom na maximálnu fitness v populácií. Potom som toto desatinné číslo medzi 0 až 1 prenásobil stomi a dosatal som počet výskytov daného jedinca v rulete. Následne sa náhodne vyberú 2 jedinci z rulety a tí sa skrížia.


**2. Turnaj**
Turnaj sa zakladá na myšlienky súboja dvoch jedincov a určenia víťaza, ktorý pokračuje v turnaji ďalej. V mojej implementácií sa vykonajú iba 2 súboje, ktoré určia jedincov-rodičov, ktorý sa skrížia. Do turnaja su náhodne vybraní 4 jedinci. Súboj zvádza prvý s druhým a tretí so štvrtým. Súboj medzi 2 jedincami vyhráva ten, ktorý má vyššiu fitness hodnotu. Ak je rovnaká, postupuje prvý. 


#### Kríženie
Aby sme potencionálne získali úspešnejšieho jedinca v novej generácií, tak dochádza k kríženiu dvoch jedincov z aktuálnej popolúcie. Najprv som skúšal uniformné kríženie, kde potomok získava rovnaký počet buniek od oboch rodičov. KOnkrétne som každú párnu bunku bral od 1.rodiča a každú nepárnu bunku od 2.rodiča. Lepšie výsledky poskytovalo jednobodové kríženie, kde sa zvolí náhodne číslo A (0 < A < počet buniek N )N. Potom sa vezme x buniek od 0 až po A od prvého rodiča a y buniek od A až po N od druhého rodiča. Samotné kríženie prebieha v konštruktore **Jedinec(rodič1, rodič2)** v triede Jedinec.

#### Mutácie
Niekedy však len odovzdanie buniek medzi generáciami vďaka križeniu nestačí a preto dochádza pri reprodukcii k náhodným zmenám v potomkoch, tzv mutáciam. Samotný algoritmus je inicializovaný s pravdebnosťou že sa daný jedinec-potomok po krížení zmutuje. Mutácie v zadaní nájdeme ako operáciu, kde sa prechádza poľom potomkov po krížení a pre každého jedinca je náhodne vygeneruje desatinné číslo medzi 0 až 1 a ak je menšie ako zadaná pravdepodobnosť mutácií, tak je jedna náhodne vybraná bunka v jedincovi zmenená na novú náhodnú hodnotu. Po testovaní som zistil, že hodnota 0.15 ponúka odhadom nalepšie výsledky.

## Porovnanie spôsobov tvorby novej genrácie




## Priestor na zlepšenie
Myslím si že priestor ako vyepšiť genetický algoritmus, by bol as najmä pri krížení a pri mutácií. Keďže výsledný pohyb hľadača nezávisí priamo od buniek ale aj od postupnosti a samotných operácií vo vykonávanom programe. Bolo by zaujímavé skúsiť nájsť, ktoré by možno vedelo rozoznávať aj operácie krížiť aj v závislosti od toho. Tiež si myslím, že by bolo možné vylepšiť mutáciu aby sa pravdepobnosť mutácií viazala priamo na bunku a nie na jedinca ako teraz. 

