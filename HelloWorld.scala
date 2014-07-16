import com.firebase.client._
import java.util.concurrent.Semaphore
import java.util.HashMap

import scala.collection.JavaConverters._

import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.index.IndexResponse
import scala.util.{Success, Failure}
import scala.concurrent._
import ExecutionContext.Implicits.global

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

import java.util.{Map => JMap}

object HelloWorld extends App {
	val ref = new Firebase("https://wutwut.firebaseIO-demo.com/")

	val client = ElasticClient.local

	//client.execute { create index "things" }

	val f: Future[SearchResponse] = client execute {
		search in "things"->"thing" query "*"
	}

	f onComplete {
		case Success(p) => println(p)
		case Failure(t) => println("An error has occured: " + t)
	}

	val x:Int = 7;

	val toSet = new HashMap[String, Any]();
	toSet.put("first", "Fred");
	toSet.put("last", "Swanson");

	val details = new HashMap[String, Any]();
	details.put("type", "person");
	details.put("color", "blue");
	details.put("number", x);

	toSet.put("details", details);

	println(toSet)

	ref.child("Fred").setValue(toSet);

	ref.addChildEventListener(new ChildEventListener() {
		def onChildAdded(snapshot:DataSnapshot, previousChildName:String) {
			val userName = snapshot.getName();

			println("User " + getChildOfSnapshot(snapshot, "details/color") + " has entered the chat");

			val i: Future[IndexResponse] = client execute { 
				index into "things/thing" fields "color"->details.toString id 1243
			}

			i onComplete {
				case Success(p) => println("Successfully added entry: " + p.getId())
				case Failure(t) => println("An error has occured: " + t)
			} 
		}

		def onChildChanged(snapshot:DataSnapshot, previousChildName:String) { }

		def onChildRemoved(snapshot:DataSnapshot) { }

		def onChildMoved(snapshot:DataSnapshot, previousChildName:String) { }

		def onCancelled(error:FirebaseError) { }
	});

	def getChildOfSnapshot(snapshot:DataSnapshot, path:String):Any = {
		val nodes = path split "/"
		nodes.foldLeft(snapshot.getValue()) (_.asInstanceOf[JMap[String, Object]] get _)
	}

	new Semaphore(0).acquire()
}