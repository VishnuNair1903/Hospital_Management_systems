import java.io.*;
import java.util.*;
import java.lang.*;

class Hospital {
    String pname;
    char gender;
    long Inc_id;
    int age;
    long phone;
    String email;
    int priority;
    boolean visited;

    public Hospital(String pname, char gender, long Inc_id, int age, long phone, String email, int priority) {
        this.pname = pname;
        this.gender = gender;
        this.Inc_id = Inc_id;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.priority = priority;
        this.visited = false; 
    }
    
}

class Splay {
    int key;
    Hospital val;
    Splay lchild;
    Splay rchild;
}

class Main{

    Splay root;
    PriorityQueue<Hospital> patientQueue;

    public Main() {
        root = null;
        patientQueue=new PriorityQueue<>(Comparator.comparing(prio->prio.priority));
    }

    Splay RR_Rotate(Splay k2) {
        Splay k1 = k2.lchild;
        k2.lchild = k1.rchild;
        k1.rchild = k2;
        return k1;
    }

    Splay LL_Rotate(Splay k2) {
        Splay k1 = k2.rchild;
        k2.rchild = k1.lchild;
        k1.lchild = k2;
        return k1;
    }

    Splay Splay(int key, Splay root) {
        if (root == null || root.key == key)
            return root;

        if (root.key > key) {
            if (root.lchild == null)
                return root;

            if (root.lchild.key > key) {
                root.lchild.lchild = Splay(key, root.lchild);
                root = RR_Rotate(root);
            } else if (root.lchild.key < key) {
                root.lchild.rchild = Splay(key, root.lchild.rchild);
                if (root.lchild.rchild != null)
                    root.lchild = LL_Rotate(root.lchild);
            }
            return (root.lchild == null) ? root : RR_Rotate(root);
        } else {
            if (root.rchild == null)
                return root;

            if (root.rchild.key > key) {
                root.rchild.lchild = Splay(key, root.rchild.lchild);
                if (root.rchild.lchild != null)
                    root.rchild = RR_Rotate(root.rchild);
            } else if (root.rchild.key < key) {
                root.rchild.rchild = Splay(key, root.rchild.rchild);
                root = LL_Rotate(root);
            }
            return (root.rchild == null) ? root : LL_Rotate(root);
        }
    }

    Splay New_Node(int key, Hospital h) {
        Splay p_node = new Splay();
        p_node.key = key;
        p_node.val = h;
        p_node.lchild = p_node.rchild = null;
        return p_node;
    }

    Splay Insert(int key, Hospital h, Splay root) {
        Splay p_node = New_Node(key, h);

        if (root == null) {
            return p_node;
        }

        root = Splay(key, root);

        if (key < root.key) {
            p_node.lchild = root.lchild;
            p_node.rchild = root;
            root.lchild = null;
            root = p_node;
        } else if (key > root.key) {
            p_node.rchild = root.rchild;
            p_node.lchild = root;
            root.rchild = null;
            root = p_node;
        } else {
            return root;
        }

        return root;
    }

    Splay Delete(int key, Splay root) {
        if (root == null)
            return null;

        root = Splay(key, root);

        if (key != root.key) {
            System.out.println(key + " is not present in the tree");
            return root;
        } else {
            Splay temp;
            if (root.lchild == null) {
                temp = root;
                root = root.rchild;
            } else {
                temp = root;
                root = Splay(key, root.lchild);
                root.rchild = temp.rchild;
            }
            return root;
        }
    }

    Splay Search(int key, Splay root) {
        return Splay(key, root);
    }

    void InOrder(Splay root) {
        if (root != null) {
            InOrder(root.lchild);
            System.out.println("key: " + root.key);
            if (root.lchild != null)
                System.out.println(" | left child: " + root.lchild.key);
            if (root.rchild != null)
                System.out.println(" | right child: " + root.rchild.key);
            System.out.println();
            InOrder(root.rchild);
        }
    }

    void DisplayAllPatients(Splay root){
        System.out.println("PATIENT DETAILS");
        InOrder(root);
    }

    private void displayPatientInfo(int patientNumber, Splay root){
        Splay result = Search(patientNumber, root);
        if (result.key == patientNumber) {
            Hospital patient = result.val;
            System.out.println("Patient Information:");
            System.out.println("Patient Name: " + patient.pname);
            System.out.println("Gender: " + patient.gender);
            System.out.println("Insurance ID: " + patient.Inc_id);
            System.out.println("Age: " + patient.age);
            System.out.println("Phone: " + patient.phone);
            System.out.println("Email: " + patient.email);
            System.out.println("Priority: " + patient.priority);
        } else {
            System.out.println("Patient with number " + patientNumber + " not found.");
        }
    }

    private void servePatientsOnPriority(){
        while(!patientQueue.isEmpty()){
            Hospital nextPatient=patientQueue.poll();
            if(!nextPatient.visited){
                System.out.println("Serving patient based on priority:"+ nextPatient.pname);
                nextPatient.visited=true;
                break;
                
            }
            
        }
    }

    public static void main(String[] args) {
        Main st = new Main();
        Splay root = null;
        int choice;
        

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\t\t\tHOSPITAL MANAGEMENT SYSTEM");
            System.out.println("1. Insert Patient");
            System.out.println("2. Delete Patient");
            System.out.println("3. Search Patient");
            System.out.println("4. Display most recently Searched Patient");
            System.out.println("5. Display All patient details stored in the Database");
            System.out.println("6. Exit Program");
            System.out.println("7. Serve Patients based on priority");
            System.out.println("8. Visit doctor");
            System.out.println("9. serach by id");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            int input;

            switch (choice) {
                case 1:
                    System.out.println("Enter Patient number to be inserted (E.g., 4): ");
                    input = scanner.nextInt();
                    System.out.println("Enter Patient name: ");
                    String pname = scanner.next();
                    System.out.println("Enter Patient Age: ");
                    int age = scanner.nextInt();
                    System.out.println("Enter Insurance ID (6 digits): ");
                    long Inc_id = scanner.nextLong();
                    System.out.println("Enter Patient Gender (M/F): ");
                    char gender = scanner.next().charAt(0);
                    System.out.println("Enter Patient Email: ");
                    String email = scanner.next();
                    System.out.println("Enter Patient Phone No (10 digits): ");
                    long phone = scanner.nextLong();
                    System.out.println("Priority of the patient ");
                    int priority = scanner.nextInt();
                    Hospital h = new Hospital(pname, gender, Inc_id, age, phone, email, priority);
                    root = st.Insert(input, h, root);
                    st.patientQueue.add(h);
                    System.out.println("After Insert: " + input);
                    st.InOrder(root);
                    break;
                case 2:
                    System.out.println("Enter Patient to be deleted: ");
                    input = scanner.nextInt();
                    root = st.Delete(input, root);
                    System.out.println("After Delete: " + input);
                    st.InOrder(root);
                    break;
                case 3:
                    System.out.println("Enter Patient No to be searched: ");
                    input = scanner.nextInt();
                    root = st.Search(input, root);
                    if (root.key == input) {
                        System.out.println("Element to be searched is found");
                        System.out.println("After Search " + input);
                        st.InOrder(root);
                    } else {
                        System.out.println("Element Not found");
                    }
                    break;
                case 4:
                    System.out.println("Most Recent Searched element: ");
                    System.out.println("Key: " + root.key);
                    System.out.println("Name: " + root.val.pname);
                    break;
                case 5:
                    st.DisplayAllPatients(root); 
                    break;
                case 6:
                    System.exit(0);
                case 7:
                    
                    st.servePatientsOnPriority();
                    break;
                case 8:
                    if (!st.patientQueue.isEmpty()) {
                        
                        Hospital nextPatient = st.patientQueue.poll();
                        System.out.println(nextPatient.pname + " is visiting the doctor.");
                        
                        nextPatient.visited=true;
                    } else {
                        System.out.println("No patients in the queue to visit the doctor.");
                    }
                    break;

                case 9:
                    System.out.println("Enter Patient Number to display information: ");
                    input = scanner.nextInt();
                    st.displayPatientInfo(input, root);
                    break;
                default:
                    System.out.println("Invalid type!");
            }
        }
    }
}