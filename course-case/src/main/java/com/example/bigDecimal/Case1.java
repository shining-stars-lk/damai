package com.example.bigDecimal;

public class Case1 {

    public static void main(String[] args) {
            int[] array = {1, 2, 3, 4, 5};
            a(array);
    }
    
    public static void a(int[] obs){
        s(obs);
    }
    public static void s(int... obs){
        for (final int ob : obs) {
            System.out.println(ob);
        }
    }
}
