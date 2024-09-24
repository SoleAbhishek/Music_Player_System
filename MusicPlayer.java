import java.io.*;
import java.util.*;

class Node {
    String song;
    Node next;
    Node prev;

    Node(String song) {
        this.song = song;
        this.next = null;
        this.prev = null;
    }
}

public class MusicPlayer {
    private Node top = null;
    private Node head = null;

    // Write song name to file
    public void toFile(String song) {
        try (FileWriter fw = new FileWriter("playlist.txt", true)) {
            fw.write(song + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    // Add new song to the list
    public void addNode() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter Song name: ");
        String song = sc.nextLine();

        Node newNode = new Node(song);
        toFile(song);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.prev = temp;
        }
    }

    // Delete song from the playlist file
    public void deleteFromFile(String songToDelete) {
        File inputFile = new File("playlist.txt");
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(songToDelete)) {
                    writer.write(line + System.lineSeparator());
                } else {
                    found = true;
                }
            }

            if (found) {
                System.out.println("Song has been deleted.");
            } else {
                System.out.println("Song not found.");
            }

            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the song.");
        }
    }

    // Delete last node (song)
    public void deleteLastNode() {
        if (head == null) {
            System.out.println("No songs in the playlist.");
            return;
        }

        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }

        if (temp.prev != null) {
            temp.prev.next = null;
        } else {
            head = null;
        }

        deleteFromFile(temp.song);
        System.out.println("Song deleted.");
    }

    // Print all songs
    public void printPlaylist() {
        Node temp = head;
        if (temp == null) {
            System.out.println("Playlist is empty.");
            return;
        }

        System.out.println("\nPlaylist:");
        while (temp != null) {
            System.out.println(temp.song);
            temp = temp.next;
        }
    }

    // Count total number of songs
    public void countSongs() {
        Node temp = head;
        int count = 0;

        while (temp != null) {
            count++;
            temp = temp.next;
        }

        System.out.println("Total songs: " + count);
    }

    // Search for a song
    public void searchSong() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter song to search: ");
        String songToSearch = sc.nextLine();

        Node temp = head;
        while (temp != null) {
            if (temp.song.equals(songToSearch)) {
                System.out.println("Song found: " + songToSearch);
                return;
            }
            temp = temp.next;
        }

        System.out.println("Song not found.");
    }

    // Play a song and add it to recently played list
    public void playSong() {
        Scanner sc = new Scanner(System.in);
        printPlaylist();
        System.out.println("\nChoose song to play: ");
        String songToPlay = sc.nextLine();

        Node temp = head;
        while (temp != null) {
            if (temp.song.equals(songToPlay)) {
                System.out.println("Now playing: " + songToPlay);
                pushToRecentlyPlayed(songToPlay);
                return;
            }
            temp = temp.next;
        }

        System.out.println("Song not found.");
    }

    // Push song to recently played list (stack behavior)
    public void pushToRecentlyPlayed(String song) {
        Node newNode = new Node(song);
        newNode.next = top;
        top = newNode;
    }

    // Display recently played songs
    public void displayRecentlyPlayed() {
        Node temp = top;
        if (temp == null) {
            System.out.println("No recently played tracks.");
            return;
        }

        System.out.println("Recently played tracks:");
        while (temp != null) {
            System.out.println(temp.song);
            temp = temp.next;
        }
    }

    // Display the last played song
    public void lastPlayedSong() {
        if (top == null) {
            System.out.println("No last played tracks.");
        } else {
            System.out.println("Last Played Song: " + top.song);
        }
    }

    // Load songs from file into playlist
    public void loadPlaylistFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("playlist.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Node newNode = new Node(line);
                if (head == null) {
                    head = newNode;
                } else {
                    Node temp = head;
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = newNode;
                    newNode.prev = temp;
                }
            }
            System.out.println("Playlist loaded from file.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
        }
    }

    // Main menu
    public static void main(String[] args) {
        MusicPlayer player = new MusicPlayer();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Add New Song\n2. Delete Last Song\n3. Display Playlist\n4. Count Songs\n5. Search Song\n6. Play Song\n7. Recently Played\n8. Last Played Song\n9. Load Playlist From File\n10. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    player.addNode();
                    break;
                case 2:
                    player.deleteLastNode();
                    break;
                case 3:
                    player.printPlaylist();
                    break;
                case 4:
                    player.countSongs();
                    break;
                case 5:
                    player.searchSong();
                    break;
                case 6:
                    player.playSong();
                    break;
                case 7:
                    player.displayRecentlyPlayed();
                    break;
                case 8:
                    player.lastPlayedSong();
                    break;
                case 9:
                    player.loadPlaylistFromFile();
                    break;
                case 10:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 10);
    }
}
