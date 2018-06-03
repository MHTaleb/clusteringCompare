/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author taleb
 */
public class Corbeille {
    
    private static final List<Player> PLAYERS = new ArrayList();
    
    public static List<Player> lireCorbeille(){
        return PLAYERS;
    }
    
    public static void inscrire(Player player){
        PLAYERS.add(player);
    }
    
    public static Player achetter(Player player ){
        return PLAYERS.remove(PLAYERS.indexOf(player));
    }
    
    
}
