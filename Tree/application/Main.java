/*
 * Widler Rislin 
 * 1/12/2020
 * Assignment 1
 * Professor Mahendra Gossai
 * CEN4025C
 * This program reads in a directory as input from the user and stores it in a tree structure
 * The tree is then displayed on both the console and a Treeview window with info about the folder.
 */

package application;
	
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {    // Code to set up the Treeview and to populate the tree
		Scanner scan = new Scanner(System.in);
		String path = null;
		
		System.out.println("Please enter folder path");
		// Checks to see if input by user is valid directory, program exits if not.
		try {
			
		 path = scan.nextLine();
		 scan.close();
		File dir = new File(path);
		
		if( !dir.exists() || dir.isFile())
			throw new Exception(); 
		}
		
		catch(Exception e) {
			System.out.println("Directory is not Valid, program has exited");
			System.exit(0);
			
		}
		
		Node node = new Node(path);		//Creates parent node of directory tree
		node=addFolder(node); // Populates node with children folder.
		showTree(node, " ");  // Outputs tree to console
		
		// Code for Treeview window
		try {  
			TreeView treeView = new TreeView();
			TreeItem rootNode= new TreeItem(node.folder + "\r\nNumber of Files: " + node.numFiles + "\r\nTotal size of Files: " + node.fileSizes);
			rootNode.setExpanded(true);
			rootNode.getChildren().add(node.item);		
			treeView.setRoot(rootNode);
			VBox root = new VBox();
			root.getChildren().add(treeView);
			Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle(node.folder + " Tree");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// Node class for storing folder info
	static class Node {
		String folder;
		Node parent=null;
		TreeItem item; // TreeItem for Treeview
		List<Node> children = new ArrayList<>(); // List of child folders
		int numFiles=0,fileSizes=0;

		
		// Node Constructor, takes folder path as a string
		public Node(String folder) {
			this.folder=folder;
			fileInfo(folder);
			item = new TreeItem(folder + "\r\nNumber of Files: " + numFiles + "\r\nTotal size of Files: " + fileSizes);
			
			
		}
		
		// adds child to parent node, and to treeitem
		public Node addChild(Node child) {
			
			child.parent=this;
			this.children.add(child);
			item.getChildren().add(child.item);
			return child;
		}
		
		// Gets number of files, and total file size of the folder		
		public void fileInfo(String folder) {
			File file = new File(folder);
			File[] files= file.listFiles();
			for(File f: files) {
				if (f.isFile())
				{ 					
					numFiles++;
					fileSizes+=f.length();
					
				}
			}
			
		}
	}
	
	//Recursively scans through the directory to find and add child folders
	static Node addFolder(Node root) {
		
		File file = new File(root.folder);
		File[] files = file.listFiles();
		if (files==null)
			return root;
		for (File f : files) {			
			if(f.isDirectory()) {
				Node child = new Node(f.getAbsolutePath());
				child=addFolder(child);
				root.addChild(child);
				
			}
			
		}
		return root;
	}
	
	//Outputs the directory tree to the console.
	static void showTree(Node root, String s) {
		
		System.out.println(s + root.folder);
		for(Node i : root.children)
		{
			showTree(i,s+s);
		}
	}
		
	public static void main(String[] args) {
		launch(args);
	}
}
