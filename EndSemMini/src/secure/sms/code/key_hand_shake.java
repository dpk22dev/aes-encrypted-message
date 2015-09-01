package secure.sms.code;

import java.math.BigInteger;

public class key_hand_shake {

	private BigInteger keys_p[] = new BigInteger[10];
	private BigInteger keys_g[] = new BigInteger[10];
	public BigInteger Xa;
	public BigInteger Ya;
	public BigInteger Yb;
	public BigInteger p;
	public BigInteger g;	
	public BigInteger key;
	
	public key_hand_shake() {
		key = new BigInteger("0");
				
		keys_p[0] = new BigInteger("240880768758109672023976650634146137069");
		keys_p[1] = new BigInteger("309321575728853820567090031872101773703");
		keys_p[2] = new BigInteger("333007110039448412290910262322228153331");
		keys_p[3] = new BigInteger("296184858108806254789367907924703414949");
		keys_p[4] = new BigInteger("254078099147540643637285576319272065709");
		keys_p[5] = new BigInteger("252677620949585797751296343691824843069");
		keys_p[6] = new BigInteger("331407909218128039197334804349949531451");
		keys_p[7] = new BigInteger("274602864679263101815526604664457446277");
		keys_p[8] = new BigInteger("289176905391825065954016185841439382993");
		keys_p[9] = new BigInteger("293772607432244844274535287726875528551");
		
		keys_g[0] = new BigInteger("4263393419");
		keys_g[1] = new BigInteger("2381385637");
		keys_g[2] = new BigInteger("2735520301");
		keys_g[3] = new BigInteger("2930853053");
		keys_g[4] = new BigInteger("4283282789");
		keys_g[5] = new BigInteger("2298732517");
		keys_g[6] = new BigInteger("3486248741");
		keys_g[7] = new BigInteger("3101636231");
		keys_g[8] = new BigInteger("2313532973");
		keys_g[9] = new BigInteger("2450025931");	
	}
	
	/**
	 * 	generating Ya
	 * @param index : index to use to get p and g 
	 */
	public void handshake(int index){
		
		p = keys_p[index];
		g = keys_g[index];
		
		String s = "";
		int r = 0 ;
		
		/**
		 * generating 20 digit random secret number
		 */
		for ( int i = 0 ; i < 20 ; i++) {
			r = (int) ( Math.random()  * 10 );
			s = s.concat(Integer.toString(r));
		}
		
		Xa = new BigInteger(s);
		
		Ya = g.modPow(Xa, p);	
		
	}
	
	/**
	 * 	Generating key
	 */
	public void getKey() {
		key = Yb.modPow(Xa, p);
	}
	
}
