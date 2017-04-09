package sample;

import java.awt.*;

/**
 * Created by Riso on 4/4/2017.
 */
public class HladacPokladov {

    private Point start;
    private Point aktPozicia = new Point();
    private int pocPokladov;
    private int pocKrokov;
    private Mapa mapa;


    public HladacPokladov(int startX, int startY, Mapa mapa) {
        this.mapa = mapa;
        start = new Point(startX, startY);
        this.reset();
    }


    // Resetuje hladaca na zaciatocnu poziciu, resetuje pocet pokladov a vynuluje pocet krokov
    public void reset() {
        mapa.resetujMapu();
        aktPozicia.setLocation(start.x, start.y);
        pocPokladov = mapa.jePoklad(start.x, start.y) == true ? 1 : 0; // Ak uz stoji na poklade tak zarata 1 poklad;
        pocKrokov = 0;
    }

    // Zaregistruje pohyb cez vypis vo virtualnom stroji a podla pismenka vyvola pohyb na suradnice.
    public void vykonajPohyb(String pohyb) throws  MimoMapyException{
        switch (pohyb) {
            case "P":
                pohyb(1, 0);
                break;
            case "H":
                pohyb(0, 1);
                break;
            case "D":
                pohyb(0,-1);
                break;
            case "L":
                pohyb(-1,0);
                break;
        }
    }

    // Aktualizuj poziciiu hladaca a vyhod vynimku ak siahne mimo mapy
    private void pohyb(int pohybX, int pohybY) throws MimoMapyException{
        aktPozicia.x += pohybX;
        aktPozicia.y += pohybY;

        // Ak sa ocitne mimo mapy vyhodi vynimku
        if(! mapa.jevMape(aktPozicia.x, aktPozicia.y)){
            throw new MimoMapyException("Hladac sa ocitol mimo mapy.");
        }

        pocKrokov++;

        // Zvysi pocet najdenych pokladov ak nasiel na novej pozicii poklad.
        if(mapa.jePoklad(aktPozicia.x, aktPozicia.y)){
            pocPokladov++;
        }


         //System.out.println("X > " + aktPozicia.x + ", Y >" + aktPozicia.y + ", POKLADY = " + pocPokladov);

    }


    public int getPocNajdenychPokladov() {
        return pocPokladov;
    }

    public int getPocKrokov() {
        return pocKrokov;
    }
}


