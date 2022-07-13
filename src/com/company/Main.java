package com.company;

import java.util.Random;

public class Main {

        public static int bossHealth = 1500;
        public static int bossDamage = 50;
        public static String bossDefendsType = "";
        public static int[] heroesHealth = {300, 300, 300, 500, 350, 400, 400, 300};
        public static int[] heroesDamage = {20, 25, 20, 5, 5, 30, 30, 20};
        public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Golomb", "Lucky", "Berserk", "Thor", "Medic"};
        public static int roundNumber = 1;
        public static int healHeroes = 80;
        public static boolean luckyPlayer, stunThor, golombBuff, randomMiss;
        public static int minValue = heroesHealth[0];

        public static void main(String[] args) {
            printStatistics();
            while (!isGameFinished()) {
                chooseDefence();
                System.out.println("ROUND " + (roundNumber++));
                round();
            }
        }

        public static void setHealHeroes() {
            if (heroesHealth[7] > 0) {
                for (int i = 0; i < heroesHealth.length; i++) {
                    if (heroesHealth[i] <= 0) {
                        heroesHealth[i] = 0;
                    }
                    if (heroesHealth[i] < 100 && heroesHealth[i] > 1) {
                        System.out.println(heroesHealth[i]);
                        for (int j = 1; j < heroesHealth.length; j++) {
                            if (heroesHealth[j] < minValue && heroesHealth[j] > 0) {
                                minValue = heroesHealth[j];
                                heroesHealth[j] = minValue + healHeroes;
                            }
                        }
                    }
                }
            }
        }

        public static void printStatistics() {
            System.out.println("Boss health: " + bossHealth + " [" + bossDamage + "]");
            for (int i = 0; i < heroesHealth.length; i++) {
                System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " [" + heroesDamage[i] + "]");
            }
            System.out.println("_______________________");
        }

        public static void chooseDefence() {
            Random random = new Random();
            int randomIndex = random.nextInt(heroesAttackType.length);
            bossDefendsType = heroesAttackType[randomIndex];
            System.out.println("Boss chose: " + bossDefendsType);

        }

        public static boolean isGameFinished() {
            if (bossHealth <= 0) {
                System.out.println("Heroes win!!!");
                System.out.println("Босс оглушен");
                return true;
            }
            boolean allHeroesDead = true;
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0) {
                    allHeroesDead = false;
                    break;
                }
            }
            if (allHeroesDead) System.out.println("Boss won!!!");
            return allHeroesDead;
        }

        public static void bossHits() {
            if (stunThor) System.out.println("Тор оглушает босса на один раунд.");
            else {
                if (heroesHealth[3] > 0) {
                    heroesHealth[3] = heroesHealth[3] + bossDamage;
                    heroesHealth[3] = heroesHealth[3] - (bossDamage + (bossDamage / 5) * heroesHealth.length) - (bossDamage / 5);
                    golombBuff = true;
                    if (heroesHealth[3] - bossDamage < 0) {
                        heroesHealth[3] = 0;  //
                    }
                } else golombBuff = false;

                if (heroesHealth.length > 0 && bossHealth > 0) {
                    Random random = new Random();
                    randomMiss = random.nextBoolean();
                    if (randomMiss && heroesHealth[4] > 0) {
                        heroesHealth[4] += bossDamage;
                        System.out.println("Медик лечить своих созников");
                        luckyPlayer = false;
                    }
                    if (!randomMiss && heroesHealth[5] > 0) {
                        heroesHealth[5] += bossDamage / 2;
                        System.out.println("Лаки уклоняется от босса");
                        luckyPlayer = true;
                    }
                }
                for (int i = 0; i < heroesHealth.length; i++) {
                    if (heroesHealth[i] > 0 && bossHealth > 0) {
                        if (heroesHealth[i] - bossDamage < 0) {
                            heroesHealth[i] = 0;
                        } else if (golombBuff) {
                            heroesHealth[i] = heroesHealth[i] - bossDamage;
                            heroesHealth[i] = heroesHealth[i] + (bossDamage / 5);
                        } else heroesHealth[i] = heroesHealth[i] - bossDamage;
                    }
                }
            }
            stunThor = false;
        }

        public static void heroesHits() {
            if (heroesHealth[6] > 0) {
                Random randomThor = new Random();
                boolean randomMiss = randomThor.nextBoolean();
                if (randomMiss) {
                    Random random1 = new Random();
                    boolean randomMiss1 = random1.nextBoolean();
                    if (randomMiss1) {
                        stunThor = true;
                    }
                }
            }
            for (int i = 0; i < heroesDamage.length; i++) {
                if (bossHealth > 0 && heroesHealth[i] > 0) {
                    if (bossHealth - heroesDamage[i] < 0) {
                        if (luckyPlayer && heroesHealth[5] > 0) {
                            bossHealth = bossHealth - heroesDamage[5] * (bossDamage / 2);
                            return;
                        }
                        bossHealth = 0;
                    } else bossHealth = bossHealth - heroesDamage[i];


                    if (bossDefendsType == heroesAttackType[i]) {
                        Random random = new Random();
                        int kef = random.nextInt(10);
                        if (bossHealth - heroesDamage[i] * kef < 0) {
                            bossHealth = 0;
                            System.out.println("Critical = " + kef * heroesDamage[i]);
                        } else
                            bossHealth = bossHealth - heroesDamage[i] * kef;
                        System.out.println("Critical = " + kef * heroesDamage[i]);
                    }

                }
            }
        }

        public static void round() {
            setHealHeroes();
            bossHits();
            heroesHits();
            printStatistics();
        }
    }

