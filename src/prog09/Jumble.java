package prog09;

import prog02.ConsoleUI;
import prog02.GUI;
import prog02.UserInterface;
import prog06.SkipMap;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class Jumble {
  /**
   * Sort the letters in a word.
   * @param word Input word to be sorted, like "computer".
   * @return Sorted version of word, like "cemoptru".
   */
  public static String sort (String word) { // fix
    char[] sorted = word.toCharArray();
    Arrays.sort(sorted);
    return new String(sorted);

  }

  public static void main (String[] args) {
    UserInterface ui = new GUI("Jumble");
    // UserInterface ui = new ConsoleUI();

    //Map<String,String> map = new LinkedMap<String,String>();
    // Map<String,String> map = new PDMap();
    // Map<String,String> map = new LinkedMap<String,String>();
   // Map<String,String> map = new BTree<String,String>();

    Map<String, List<String>> map= new BTree<String, List<String>>(); //PART 11**!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!***


    Scanner in = null;
    do {
      try {
        in = new Scanner(new File(ui.getInfo("Enter word file.")));
      } catch (Exception e) {
        System.out.println(e);
        System.out.println("Try again.");
      }
    } while (in == null);
	    
    int n = 0;
    while (in.hasNextLine()) {
      String word = in.nextLine();
      if (n++ % 1000 == 0)
	      System.out.println(word + " sorted is " + sort(word));
      
      // EXERCISE: Insert an entry for word into map.
      String sorted = sort(word);
      if(!map.containsKey(sorted)){
        List<String> wordList = new ArrayList<>();
        wordList.add(word);
        map.put(sort(word),wordList);
      }
      else{
        List<String> wordList = map.get(sorted);
        wordList.add(word);
      }





    }

    String jumble = ui.getInfo("Enter jumble.");
    while(jumble != null){
      List<String> words = map.get(sort(jumble));
      if(words == null)
        return;
      else
        ui.sendMessage(jumble + " unjjumble is " + words);


      jumble = ui.getInfo("Enter jumble.");
    }


    while (true){  //LOOK HERE TODO
      String clue = ui.getInfo("Enter letters from clue");
      if(clue == null){
        return;
      }
      clue = sort(clue);
      int l = 0;
      do {
        String numLetters = ui.getInfo("How many letters in first word: ");
        l = Integer.parseInt(numLetters);
      }

      while (l <=0);

      //boolean foundMatch=false;
      for (String key1 : map.keySet())
      {
        if(key1.length() == l)
        {
          String key2 = "";
          int key1Index = 0;
          for(int i = 0; i<clue.length(); i++)
          {
            //TODO
            char c =clue.charAt(i);


            if(key1Index>=key1.length())
            {
              key2+=c;
            }
            else if(c==key1.charAt(key1Index))
            {
              key1Index++;
              //System.out.println("Key1Index " + key1Index + "\nI: " + i);
            }
            else if(c > key1.charAt(key1Index))
            {
              break;
            }
            else
            {
              key2+=c;
            }
          } // for letters in the sorterd clue
          if(key1Index>=key1.length() && map.containsKey(key2))
          {
//            foundMatch=true;
            ui.sendMessage(map.get(key1) + " " + map.get(key2));
          }

        }//if the key matches the length of the clue

      }//For key1 in keyset


    }
  }


}

//        if(!foundMatch){
//                ui.sendMessage("Sorry, no matches found for " + clue + " with length " + l);
//                }
    

