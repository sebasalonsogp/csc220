package prog05;

import prog02.GUI;
import prog02.UserInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class NotWordle {

    UserInterface ui;

    List<Node> wordEntries = new ArrayList<Node>();

    private class Node
    {

        protected String word;
        protected Node next;

        public Node(String word) {
            this.word = word;
            this.next = null;
        }
    }




     NotWordle(UserInterface ui)
     {

        this.ui=ui;
    }

    void loadWords(String file)
    {
        boolean valid =false;
        while(!valid) {
            try
            {
                Scanner scnr = new Scanner(new File(file));
                while (scnr.hasNextLine()) {
                    wordEntries.add(new Node(scnr.nextLine()));
                }
                scnr.close();
                valid = true;
            }
            catch (FileNotFoundException e)
            {
                ui.sendMessage("File not found! " + e);
                file=ui.getInfo("Enter a valid file name: ");

            }
        }

    }


    public Node find(String word)
    {

        for(Node node : wordEntries)
        {
            if(node.word.equals(word))
                return node;

        }

        return null;
    }



    public static void main(String[] args) {
         GUI ui = new GUI("NotWordle Game");
        NotWordle game = new NotWordle(ui);
        String filename = ui.getInfo("Enter the file name: ");
        game.loadWords(filename);

        String start = ui.getInfo("Inset a starting word");
        String target = ui.getInfo("Inset a target word");


        String[] commands = {"Human Plays.","Computer Plays."};

        switch(ui.getCommand(commands)){
            case -1:
                    return;
            case 0:
//                if(start == null || target== null)
//                    return;
                game.play(start, target);
                break;
            case 1:
                game.solve(start, target);
                break;
        }


    }
    void play(String start1, String end1)
    {
         boolean win=false;
        String oldStart;

         while(!win)
         {
             ui.sendMessage("Current word:  " + start1 + "\nTarget word: "+ end1);
             oldStart=start1;

              start1 =ui.getInfo("Enter the next word!");

              if(start1==null)
                  break;


             while(find(start1)==null)
             {
                 ui.sendMessage(start1 + " is not a valid word!");
                 start1=oldStart;
                 break;

             }
             if(!oneLetterDifferent(oldStart,start1))
             {
                 ui.sendMessage(start1 + " does not differ by exactly one.");
                 start1 = oldStart;

             }
              if(start1.equals(end1))
              {
                    ui.sendMessage("You win!");
                    win=true;
                    return;
              }


         }
         return;

    }

    void solve(String start,String end)
    {

        Queue<Node> queue = new ArrayQueue<Node>();
        Node startNode =  find(start);
        queue.offer(startNode);

        while(!queue.isEmpty())
        {

            Node theNode = queue.poll();

            for(Node next : wordEntries)
            {
                if (next != startNode && next.next == null && oneLetterDifferent(next.word, theNode.word))
                {
                    next.next = theNode;
                    queue.offer(next);
//                    Node nextNode = next;
//                    nextNode.next = theNode;
//                    queue.offer(nextNode);

                    if(next.word.equals(end))
                    {
                       //String s= theNode.word + "\n" + end;
                        String s="";
                        for(Node node = next; node != null; node=node.next ) // TODO: FIX
                        {
                           s = node.word + "\n" + s;
                            //s = s + "\n " +node.word;
                        }
//                        while(theNode != startNode){ // TODO: FIX
//                            theNode=theNode.next;
//                            s = theNode.word + "\n" + s;
//                        }
                        //s = s + " " + startNode;

                        ui.sendMessage(s);
                        return;
                    } //if statement that checks for success

                }//IF statement that checks for conditions

            }//initial for loop that iterates through wordEntries


        }//while !isEmtpy() loop

    }//solve()

    static boolean oneLetterDifferent(String a, String b)
    {
         if(a.length() == b.length())
         {

             int cnt =0;
             for(int i =0;i<a.length();i++)
             {
                 if(a.charAt(i) != b.charAt(i))
                 {
                     cnt++;
                 }
             }
             return cnt == 1;

         }

         return false;
    } // oLD method end

} // MAIN CLASS END BRACKETS
