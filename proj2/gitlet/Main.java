package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Noenx
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0)
        {
            System.err.println("No command!");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                if (args.length < 2)
                {
                    System.out.println("No filename specified.");
                    return;
                }
                String filename = args[1];
                Repository.add(filename);
                break;
            case "commit":
                if  (args.length < 2)
                {
                    System.out.println("No message specified.");
                }
                String message = args[1];
                Repository.commit(message);
                break;
            case "rm":
                if  (args.length < 2)
                {
                    System.out.println("No filename specified.");
                }
                Repository.rm(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.global_log();
                break;
            case "find":
                if  (args.length < 2)
                {
                    System.out.println("No message specified.");
                }
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                if(args[1].equals("--"))
                {
                    Repository.checkout(args[2]);
                }
                else if(args.length == 2)
                {
                    Repository.checkoutBranch(args[1]);
                }
                else if(args[2].equals("--"))
                {
                    Repository.checkout(args[1], args[3]);
                }
                else
                {
                    System.err.println("Invalid command!");
                }
        }
    }
}
