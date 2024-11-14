import java.util.*;


public class BankY {
    
    // Inner class to handle bank account operations
    static class BankAccount {
        private String accountNumber;
        private String accountHolder;
        private double balance;

        public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
            this.accountNumber = accountNumber;
            this.accountHolder = accountHolder;
            this.balance = initialBalance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAccountHolder() {
            return accountHolder;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.printf("Deposited: Rs.%.2f. New balance: Rs.%.2f%n", amount, balance);
            } else {
                System.out.println("Invalid deposit amount.");
            }
        }

        public void withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.printf("Withdrew: Rs.%.2f. New balance: Rs.%.2f%n", amount, balance);
            } else {
                System.out.println("Invalid or insufficient funds for withdrawal.");
            }
        }

        public void transfer(BankAccount recipient, double amount) {
            if (amount > 0 && amount <= balance) {
                this.withdraw(amount);
                recipient.deposit(amount);
                System.out.printf("Transferred: Rs.%.2f to %s%n", amount, recipient.getAccountHolder());
            } else {
                System.out.println("Transfer failed: Invalid amount or insufficient funds.");
            }
        }
    }

    // Main class for the BankY system
    private HashMap<String, BankAccount> accounts = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\nWelcome to BankY");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Check Balance");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: 
                    createAccount();
                    break;
                case 2:
                    depositFunds();
                    break;
                case 3:
                    withdrawFunds();
                    break;
                case 4:
                    transferFunds();
                    break;
                case 5:
                    checkBalance();
                    break;
                case 6: {
                    System.out.println("Thank you for using BankY!");
                    return;
                }
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account number already exists. Please try a different number.");
            return;
        }
        
        System.out.print("Enter account holder name: ");
        String accountHolder = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (initialBalance < 0) {
            System.out.println("Initial balance cannot be negative.");
            return;
        }

        BankAccount account = new BankAccount(accountNumber, accountHolder, initialBalance);
        accounts.put(accountNumber, account);
        System.out.println("Account created successfully!");
    }

    private void depositFunds() {
        BankAccount account = findAccount();
        if (account != null) {
            System.out.print("Enter deposit amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            account.deposit(amount);
        }
    }

    private void withdrawFunds() {
        BankAccount account = findAccount();
        if (account != null) {
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            account.withdraw(amount);
        }
    }

    private void transferFunds() {
        System.out.print("Enter your account number: ");
        String senderAccountNumber = scanner.nextLine();
        BankAccount sender = accounts.get(senderAccountNumber);

        if (sender != null) {
            System.out.print("Enter recipient account number: ");
            String recipientAccountNumber = scanner.nextLine();
            
            if (recipientAccountNumber.equals(senderAccountNumber)) {
                System.out.println("Cannot transfer to the same account.");
                return;
            }
            
            BankAccount recipient = accounts.get(recipientAccountNumber);

            if (recipient != null) {
                System.out.print("Enter transfer amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                sender.transfer(recipient, amount);
            } else {
                System.out.println("Recipient account not found.");
            }
        } else {
            System.out.println("Sender account not found.");
        }
    }

    private void checkBalance() {
        BankAccount account = findAccount();
        if (account != null) {
            System.out.printf("Account Holder: %s%n", account.getAccountHolder());
            System.out.printf("Account Number: %s%n", account.getAccountNumber());
            System.out.printf("Current balance: Rs.%.2f%n", account.getBalance());
        }
    }

    private BankAccount findAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
        }
        return account;
    }

    public static void main(String[] args) {
        new BankY().start();
    }
}