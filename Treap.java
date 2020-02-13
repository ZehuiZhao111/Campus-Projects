package edu.stevens.cs570.assignments;

import java.util.Random;
import java.util.Stack;
/**
 * Treap class
 * 
 * @author Zehui Zhao
 **/
public class Treap<E extends Comparable<E>> {
	
	private class Node<E>{
		//data field for Node<E>
		public E data;//key
		public int priority;//priority
		public Node<E>left;//left child node
		public Node<E>right;//right child node
		
		/**
		 * constructor
		 * @param data
		 * @param priority
		 */
		public Node(E data,int priority) {
			if (data == null) {
				throw new IllegalArgumentException("the data is null,please try again!");
			}else {
				this.data = data;
				this.priority = priority;
				this.left = null;
				this.right = null;
			}		
		}
		
		/**
		 * change the current rootNode.left to the new rootNode
		 * @return new rootNode
		 */		
		public Node<E> rotateRight() {
			Node<E> currRoot = this;
			Node<E> newRoot = this.left;
			currRoot.left = newRoot.right;
			newRoot.right = this;
			return newRoot;	
		}
		
		/**
		 * change the current rootNode.right to the new rootNode
		 * @return new rootNode
		 */
		public Node<E> rotateLeft() {
			Node<E> currRoot = this;
			Node<E> newRoot = this.right;
			currRoot.right = newRoot.left;
			newRoot.left = this;
			return newRoot;
			
		}	
	}
	
	//data field for Treap		
	private Random priorityGenerator;
	private Node<E> root;
	
	/**
	 * constructor
	 * create an empty treap,use current time as seed
	 */
	public Treap() {
		this.priorityGenerator = new Random();	
		this.root = null;
	}
	
	/**
	 * constructor
	 * create an empty treap,use param "seed" as seed
	 * @param seed
	 */
	public Treap(long seed) {
		this.priorityGenerator = new Random(seed);
		this.root = null;
	}
	
	/**
	 * helper function,reorganize the treap
	 * @param curr
	 * @param path
	 */
	public void reheap(Node<E> curr, Stack<Node<E>> path) {
		while (!path.isEmpty()) {//while path isn't empty��this is a iterative version
			Node<E> parent = path.pop();
			if (parent.priority < curr.priority){
				if (parent.data.compareTo(curr.data) > 0) {
					curr = parent.rotateRight();
				} else {
					curr = parent.rotateLeft();
				}
				if (!path.isEmpty()) {
					if (path.peek().left == parent) {
						path.peek().left = curr;
					} else {
						path.peek().right = curr;
					}
				} else { 
					this.root = curr;
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * insert a new node into the treap
	 * @param key
	 * @param priority
	 * @return
	 */
	public boolean add(E key,int priority) {
		if (root == null) {
			root = new Node<E>(key, priority);
			return true;
		} else {
			Node<E> temp = new Node<E>(key, priority);
			Stack<Node<E>> stackTemp = new Stack<Node<E>>();
			Node<E> currentRoot = root;
			while(currentRoot != null) {
				int comparison = currentRoot.data.compareTo(key);
				if (comparison == 0) {
					System.out.println("add failed,already have a node containing the given key: " + key);
					return false;
				} else {
					if (comparison < 0) {
						stackTemp.push(currentRoot);
						if (currentRoot.right == null) {
							currentRoot.right = temp;
							reheap(temp, stackTemp);
							return true;
						} else {
							currentRoot = currentRoot.right;
							//add(key, priority);//do not do it recursivly,this will cause StackOverFlow error
						}
					} else {
						stackTemp.push(currentRoot);
						if (currentRoot.left == null) {
							currentRoot.left = temp;
							reheap(temp, stackTemp);
							return true;
						} else {
							currentRoot = currentRoot.left;
							//add(key, priority);//do not do it recursivly,this will cause StackOverFlow error
						}
					}
				}
			}
			return false;
		}
	}
	
	/**
	 * add wrapper class
	 * @param key
	 * @return
	 */
	public boolean add(E key) {
		return add(key, priorityGenerator.nextInt());
	}
	
	/**
	 * delete a node from the treap
	 * @param key
	 * @param root
	 * @return root
	 */
	private Node<E> delete(E key, Node<E> root){
		if (root == null) {
			return root;
		} else {
			if (root.data.compareTo(key) < 0) {
				root.right = delete(key, root.right);//do it recursivly
			} else {
				if (root.data.compareTo(key) > 0) {
					 root.left = delete(key, root.left);
				} else {
					if (root.right == null) {
						root = root.left;
					} else if (root.left == null) {
						root = root.right;			
					} else {
						if (root.right.priority < root.left.priority) {
							root = root.rotateRight();
							root.right = delete(key, root.right);
						} else {
							root = root.rotateLeft();
							root.left = delete(key, root.left);
						}
					}
				}
			}
		}
		return root;
	}
	
	/**
	 * delete wrapper class
	 * @param key
	 * @return
	 */
	public boolean delete(E key) {
		if(find(key)==true && root!=null) {
			root = delete(key, root);
			return true;	
		}else {
			System.out.println("delete failed,no such key: " + key + " exists");
			return false;			
		}
		
	}
	
	/**
	 * find if the given key exists
	 * @param root
	 * @param key
	 * @return
	 */
	private boolean find(Node<E> root,E key) {
		if(root == null) {
			return false;
		}else {
			if (root.data == key) {
				return true;
			}else{
				return find(root.left, key)||find(root.right, key);
			}
		}
	}
	
	/**
	 * find wrapper class
	 * @param key
	 * @return
	 */
	public boolean find(E key) {
		return find(root, key);
	}
	
	private String toString(Node<E> root,int depth) {
		StringBuilder r = new StringBuilder();
		for (int i=0;i<depth;i++) {
			r.append("--");
		}
		if (root==null) {
			r.append("null");
		} else {
			r.append("(key = " + root.data +", priority = " + root.priority + ")" );
			r.append("\n");
			r.append(toString(root.left, depth+1));
			r.append("\n");
			r.append(toString(root.right, depth+1));
		}
		return r.toString();		
	}
	
	public String toString() {
		return toString(root,0);
	}
	
	public static void main(String[] args) {
		Treap<Integer> testTree = new Treap<Integer>();
		testTree.add(4,19); 
		testTree.add(2,31);
		testTree.add(6,70); 
		testTree.add(1,84);
		testTree.add(3,12); 
		testTree.add(5,83);
		testTree.add(7,26);
		//testTree.delete(7);
		System.out.println(testTree.toString());

	}

}
