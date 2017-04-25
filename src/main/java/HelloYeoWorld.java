package de.trireme;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class HelloYeoWorld {
	public static void main(String[] args) {
		// testDB();
		testDBiter();
		testDBTraversal();
	}

	public static void testDB() {
		GraphDatabaseService graphDb = new EmbeddedGraphDatabase(
				"var/yeoGraphDb");
		Node firstNode = null;
		Node secondNode = null;
		Relationship relationship = null;
		Transaction tx = graphDb.beginTx();
		try {
			firstNode = graphDb.createNode();
			secondNode = graphDb.createNode();
			relationship = firstNode.createRelationshipTo(secondNode,
					MyRelationshipTypes.KNOWS);
			firstNode.setProperty("message", "Hello, ");
			secondNode.setProperty("message", "world!");
			relationship.setProperty("message", "brave Yeo in Neo4j ");
			System.out.print(firstNode.getProperty("message"));
			System.out.print(relationship.getProperty("message"));
			System.out.print(secondNode.getProperty("message"));
			tx.success();
		} finally {
			tx.finish();
		}
		graphDb.shutdown();
	}

	public static void testDBiter() {
		GraphDatabaseService graphDb = new EmbeddedGraphDatabase(
				"var/yeoGraphDb");
		Transaction tx = graphDb.beginTx();
		try {
			for (Node node : graphDb.getAllNodes()) {
				printNodePropertiesAndRelationships(node);
			}
			for (RelationshipType relType : graphDb.getRelationshipTypes()) {
				System.out.println(relType.toString());
			}
			tx.success();
		} finally {
			tx.finish();
		}
		graphDb.shutdown();
	}

	public static void testDBTraversal() {
		System.out.println(" --------------------------------- ");
		System.out.println(" Traversal test ");
		System.out.println(" --------------------------------- ");
		GraphDatabaseService graphDb = new EmbeddedGraphDatabase(
				"var/yeoGraphDb");
		Transaction tx = graphDb.beginTx();
		try {
			Node startNode = graphDb.getNodeById(1);
			Traverser traverser = startNode.traverse(
					Traverser.Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH,
					new ReturnableEvaluator() {
						public boolean isReturnableNode(TraversalPosition pos) {
							return pos.currentNode().hasRelationship(
									MyRelationshipTypes.KNOWS, Direction.BOTH);
						}
					}, MyRelationshipTypes.KNOWS, Direction.BOTH);
			for (Node node : traverser) {

				TraversalPosition currentPosition = traverser.currentPosition();
				// Do something with the node and/or its position in the
				// traversal

				System.out.println(" --------------------------------- ");
				System.out.println(" Traverser Node test ");
				System.out.println(" --------------------------------- ");
				printNodePropertiesAndRelationships(node);
				System.out.println(" --------------------------------- ");
				System.out.println(" Traverser Node test ");
				System.out.println(" --------------------------------- ");

				System.out.println(" --------------------------------- ");
				System.out.println(" Current Node test ");
				System.out.println(" --------------------------------- ");
				Node currNode = currentPosition.currentNode();

				printNodePropertiesAndRelationships(currNode);

				System.out.println(" --------------------------------- ");
				System.out.println(" Current Node test Ende");
				System.out.println(" --------------------------------- ");
			}
			tx.success();
		} finally {
			tx.finish();
		}

		graphDb.shutdown();
		System.out.println(" --------------------------------- ");
		System.out.println(" Traversal test Ende");
		System.out.println(" --------------------------------- ");
	}

	private static void printNodePropertiesAndRelationships(Node node) {
		System.out.println(" --------------------------------- ");
		System.out.println("Node id " + node.getId());
		for (String nkey : node.getPropertyKeys()) {
			
			System.out.println("Node Prop key: " + nkey);
			String nvalue = (String) node.getProperty(nkey);
			System.out.println("Node Prop value: " + nvalue);
			
			for (Relationship rel : node.getRelationships()) {
				for (String key : rel.getPropertyKeys()) {
					System.out.println("Rel Prop key: " + key);
					String rValue = (String) rel.getProperty(key);
					System.out.println("Rel Prop value: " + rValue);
				}
			}
			
		}
	}

}
