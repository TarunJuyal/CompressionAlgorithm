import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class FrequencyNode implements Comparable<FrequencyNode>{
	char data;
	int count;
	FrequencyNode left,right;
	FrequencyNode(char data,int count){
		this.data=data;
		this.count=count;
	}
	public FrequencyNode() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public int compareTo(FrequencyNode o) {
		return this.count-o.count;
	}
}
class Heap<T extends Comparable<T>>{
	ArrayList<T> data=new ArrayList<>();
	int size;
	int getSize() {
		return size;
	}
	void add(T node) {
		data.add(node);
		upHeapify(data.size()-1);
		size++;
	}
	int compare(T one,T two) {
		return one.compareTo(two);
	}
	void upHeapify(int childIndex) {
		int parentIndex=(childIndex-1)/2;
		if(compare(data.get(childIndex), data.get(parentIndex))>0) {
			swap(childIndex,parentIndex);
			upHeapify(parentIndex);
		}
	}
		void swap(int childIndex,int parentIndex) {
		T childValue=data.get(childIndex);
		T parentValue=data.get(parentIndex);
		data.set(childIndex, parentValue);
		data.set(parentIndex, childValue);
	}
	boolean isEmpty() {
		return data.size()==0;
	}
	void downHeapify(int parentIndex) {
		int leftChildIndex=2*parentIndex+1;
		int rightChildIndex=2*parentIndex+2;	
		if( leftChildIndex<data.size() && compare(data.get(leftChildIndex), data.get(parentIndex))>0) {
			swap(leftChildIndex, parentIndex);
			downHeapify(leftChildIndex);
		}
		if(rightChildIndex<data.size() && compare(data.get(rightChildIndex), data.get(parentIndex))>0) {
			swap(rightChildIndex, parentIndex);
			downHeapify(rightChildIndex);
		}
	}
	T delete() {
		swap(0,data.size()-1);
		T deleted=data.remove(data.size()-1);
		downHeapify(0);
		size--;
		return deleted;
	}
	void print() {
		for(T i:data)
			System.out.println(i+" , ");
	}

}
class HuffmanCoding{
	HashMap<Character, String> encoder;
	HashMap<String, Character> decoder;
	HashMap<Character,Integer> createFrequencyMap(String s){
		HashMap<Character, Integer> temp=new HashMap<>();
		for(int i=0;i<s.length();i++) {
			if(temp.get(s.charAt(i))!=null) {
				int count=temp.get(s.charAt(i));
				count+=1;
				temp.put(s.charAt(i), count);
			}
			else {
				temp.put(s.charAt(i), 1);
			}
		}
		return temp;
	}
	Heap<FrequencyNode> createMinHeap(HashMap<Character, Integer> fm){
		Heap<FrequencyNode> temp=new Heap<>();
		for(Map.Entry<Character,Integer> frequencies:fm.entrySet()) {
			FrequencyNode node=new FrequencyNode(frequencies.getKey(),frequencies.getValue());
			temp.add(node);
		}
		return temp;
	}
	void combineHeap(Heap<FrequencyNode> heap) {
		while(heap.getSize()!=1) {
			FrequencyNode first=heap.delete();
			FrequencyNode second=heap.delete();
			FrequencyNode node =new FrequencyNode();
			node.left=first;
			node.right=second;
			node.count=first.count+second.count;
			node.data='\0';
			heap.add(node);
		}
	}
	private void encodeAndDecode(FrequencyNode lastNode,String result) {
		if(lastNode==null)
			return;
		if(lastNode.left==null && lastNode.right==null) {
			encoder.put(lastNode.data, result);
			decoder.put(result, lastNode.data);
		}
		encodeAndDecode(lastNode.left,result+"0");
		encodeAndDecode(lastNode.right, result+"1");
	}
	void compress(String s) {
		HashMap<Character, Integer> frequencyMap=createFrequencyMap(s);
		Heap<FrequencyNode> heap=createMinHeap(frequencyMap);
		combineHeap(heap);
		FrequencyNode lastNode=heap.delete();
		encoder=new HashMap<>();
		decoder=new HashMap<>();
		encodeAndDecode(lastNode,"");
		System.out.println("Encoder: "+encoder);
		System.out.println("Decoder: "+decoder);
	}
	String encodedResult(String stringToEncode) {
		String result="";
		for(char c:stringToEncode.toCharArray())
			result+=encoder.get(c);
		return result;
	}
	String decodedResult(String stringToDecode) {
		String result = "";
		String key = "";
		for(char c : stringToDecode.toCharArray()) {
			key =  key + c;
			if(decoder.containsKey(key)) {
			 result+= decoder.get(key);
			 key = "";
			}
		}
		return result;
	}
}
public class CompressionAlgo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s="aaabbcdaabcddda";
		HuffmanCoding hc=new HuffmanCoding();
		hc.compress(s);		
		String encoded=hc.encodedResult(s);
		System.out.println("Encoded string: "+encoded);
		String decoded=hc.decodedResult(encoded);
		System.out.println("Decoded string: "+decoded);
	}

}
