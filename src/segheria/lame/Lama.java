package segheria.lame;

import java.time.LocalDateTime;

public class Lama {
	public int id;
	public String name;
    public double metriUso;
    public double oreUso;
    public int maxMetri;
    public int maxOre;
    public LocalDateTime ultimaAffilatura;
    public LocalDateTime ultimoCambio;
    
    public Lama(String nm) {
    	id = -1;
    	name = nm;
    	metriUso = 0;
    	oreUso = 0;
    	maxMetri = 0;
    	maxOre = 0;
    	ultimaAffilatura = LocalDateTime.now();
    	ultimoCambio = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return name;
    }
}
