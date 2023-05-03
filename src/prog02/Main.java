package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 *
 * @author vjm
 */
public class Main {

    /**
     * Processes user's commands on a phone directory.
     *
     * @param fn The file containing the phone directory.
     * @param ui The UserInterface object to use
     *           to talk to the user.
     * @param pd The PhoneDirectory object to use
     *           to process the phone directory.
     */
    public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
        pd.loadData(fn);
        boolean changed = false;

        String[] commands = {"Add/Change Entry", "Look Up Entry", "Remove Entry", "Save Directory", "Exit"};

        String name, number, oldNumber;

        while (true) {
            int c = ui.getCommand(commands);

            switch (c) {
                case -1: //x button
                    ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
                    break;
                case 0: // add entry
                    name = ui.getInfo("Enter the name ");
                    if(name == null)
                    {
                        break;
                    }
                    number = ui.getInfo("Enter the number");
                    if(number==null)
                    {
                        break;
                    }
                    if(name.equals(""))
                    {
                        ui.sendMessage("No blank names allowed!");
                        break;
                    }
                    if(pd.addOrChangeEntry(name, number) != null){
                        ui.sendMessage("Changed number successfully!");
                        break;
                    }
                    else
                    {
                        pd.addOrChangeEntry(name, number);
                        ui.sendMessage("Added entry successfully!");
                        changed=true;
                    }

                    break;

                case 1://lookup
                    name = ui.getInfo("Enter the name ");
                    if(name == null)
                    {
                        break;
                    }
                    if(name.equals(""))
                    {
                        ui.sendMessage("No blank names allowed!");
                        break;
                    }
                    if(pd.lookupEntry(name) == null)
                    {
                        ui.sendMessage("Name not found!");
                        break;
                    }
                    number = pd.lookupEntry(name);
                    ui.sendMessage(name + " has number " + number);

                    break;
                case 2: // remove entry
                    name = ui.getInfo("Enter the name you want to remove");
                    if(name == null)
                    {
                        ;
                        break;
                    }
                    if(name.equals(""))
                    {
                        ui.sendMessage("You must enter a valid name!");
                    }

                    //number= pd.lookupEntry(name);
                    if(pd.removeEntry(name)==null){
                            ui.sendMessage("Name not found!");
                            break;
                        }
                    else {
                            pd.removeEntry(name);
                            ui.sendMessage("Successfully removed entry.");
                            changed=true;
                        }

                    break;

                case 3: // save
                    pd.save();
                    ui.sendMessage("Saved successfully");
                    changed=false;

                    break;

                case 4: // exit
                    String[] yesno = {"YES", "NO"};
                    if(changed!=true){
                        return;
                    }
                    if(changed!=false)
                    {

                        ui.sendMessage("Would you like to exit without saving?");
                        switch(ui.getCommand(yesno))
                        {
                            case 0:
                                return;
                            case 1:
                                break;
                        }
                    }


            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fn = "csc220.txt";
        //PhoneDirectory pd = new ArrayBasedPD();
        PhoneDirectory pd = new SortedPD();
        //UserInterface ui = new ConsoleUI();

        UserInterface ui = new GUI("Phone Directory");
        //UserInterface ui = new TestUI("Phone Directory");
        processCommands(fn, ui, pd);
    }
}
