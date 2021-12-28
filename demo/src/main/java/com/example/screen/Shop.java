package com.example.screen;

import java.util.List;
import java.util.ArrayList;

public enum Shop {
    Lancer{
        public List<String> info(){
            List<String> res = new ArrayList<>();
            res.add("**********");
            res.add(String.format("*%-8s*", Shop.valueOf("Lancer")));
            res.add(String.format("*%-8d*", cost()));
            res.add("**********");
            return res;
        }

        public int cost(){
            return 100;
        }
    },
    Archer{
        public List<String> info(){
            List<String> res = new ArrayList<>();
            res.add("**********");
            res.add(String.format("*%-8s*", Shop.valueOf("Archer")));
            res.add(String.format("*%-8d*", cost()));
            res.add("**********");
            return res;
        }

        public int cost(){
            return 100;
        }
    };
    public abstract List<String> info();
    public abstract int cost();
}
