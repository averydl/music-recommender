package org.derek.assignment4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Digraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Recommender {
    private Graph users;
    private Graph userArtists;

    public Recommender() {
        try {
            users = loadUsers("/dataset/user_friends.dat");
        } catch(FileNotFoundException fnfe) {
            System.out.println("Could not open user-friend file.");
            fnfe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Recommender r = new Recommender();
        System.out.println(r);
    }

    /*
     * creates a graph from a line-separated file of user friendships
     * (two whitespace-separated integers per line represent these relationships)
     * in which vertex indices represent user id, and
     * edges between vertices represent friendships between two users
     * with the id's corresponding to each vertex of the edge
     */
    public Graph loadUsers(String fileName) throws FileNotFoundException {
        int maxId = 0; // maximum value of a user id (used to determine size of the user graph)
        Scanner in = new Scanner(new File(fileName)); // Scanner to parse user data file
        ArrayList<Integer> user1 = new ArrayList<>(); // first user in user-to-user connection
        ArrayList<Integer> user2 = new ArrayList<>(); // second user in user-to-user connection
        int line = 0; // current line in file being parsed

        // parse file and add the id of each user-to-user connection
        // to two separate array lists at the same index
        while(in.hasNextLine()) {
            line++;
            String[] curLine = in.nextLine().split("\t");
            if(curLine.length > 1) {
                try { // add user id pairs to separate lists; add unique users to 'users' set
                    int id1 = Integer.parseInt(curLine[0]);
                    int id2 = Integer.parseInt(curLine[1]);
                    if(id1 > maxId) {
                        maxId = id1;
                    } else if(id2 > maxId) {
                        maxId = id2;
                    }
                    user1.add(id1);
                    user2.add(id2);
                } catch(NumberFormatException nfe) {
                    System.out.println("Discarded user connection at line " + line + ":");
                    System.out.println("could not parse " + curLine[0] + " and " + curLine[1] + " as integers");
                }
            }
        }

        // create new user graph and add edges between connected users
        Graph graph = new Graph(maxId);
        for(int i = 0; i < user1.size()+1; i++) {
            graph.addEdge(user1.get(i)-1, user2.get(i)-1);
        }
        return graph;
    }

    @Override
    public String toString() {
        return users.toString();
    }
}
