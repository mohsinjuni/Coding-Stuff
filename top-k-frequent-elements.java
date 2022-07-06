class Solution {
    class Node{
        int element;
        int frequency;
        public Node(int e, int f){
            this.element = e;
            this.frequency = f;
        }
    }
    
    public int[] topKFrequent(int[] nums, int k) {
        
        // I am going to assume that we have this method which will return us a map of elements and their frequencies. 
        Map<Integer, Integer> frequencyMap = getFrequencyMap(nums);
        
        // Once we get element frequency, we can use this to generate an array of objects with each object 
        // containing an element and its frequency. Since we want to use quick-select, we can do partitioning around frequency.
        // I am also going to assume that we have a Node class which has two memebers, element and its frequency. With that,
        // let's further assume that we have this method to create a nodes array.
        Node[] nodesArray = buildNodesArrayFromMap(frequencyMap);
        
        // Now, we would like to partition this nodesArray on Kth index but we will do comparisons on frequency.
        // After running our quick-select algorithm, we will have nodesArray partitioned. Since Java passes objects as
        // copy-by-reference, we don't need to return anything from the below function.
        runQuickSelect(nodesArray, 0, nodesArray.length-1, nodesArray.length-k);
        
        // Once partitioned, we want to extract top-K nodes from the nodesArray from the end. 
        Node[] topKFrequentNodes = extractTopKFrequentNodes(nodesArray, k);

        // I am going to further assume that we have a sort method which is going to sort nodes by frequency in descending order. 
        sortByFrequencyinDescendingOrder(topKFrequentNodes);

        // Now we can extract first K elements from the array which should give us what we are looking for.
        int[] result = extractTopKFrequentElements(topKFrequentNodes);
        
        return result;        
    }
    void runQuickSelect(Node[] nodes, int start, int end, int k){
        if(start >= end) return;
        int pivot_index = partition(nodes, start, end);
        if(pivot_index > k){
            runQuickSelect(nodes, start, pivot_index-1, k);
        }else if(pivot_index < k){
            runQuickSelect(nodes, pivot_index+1, end, k);
        }
    }
    int partition(Node[] nodes, int start, int end){
        int size = end - start + 1;
        Random rand = new Random();
        int randIndex = rand.nextInt(size) + start;
        Node randNode = nodes[randIndex];

        // Lumoto's partitioning
        int origStart = start;
        swap(nodes, start, randIndex);
        int pointer = start + 1;
        while(pointer <= end){
            if(nodes[pointer].frequency <= randNode.frequency){
                start++;
                swap(nodes, pointer, start);
            }
            pointer++;
        }
        swap(nodes, origStart, start);
        return start;
    }
    void swap(Node[] A, int left, int right){
        Node temp = A[left];
        A[left] = A[right];
        A[right] = temp;
    }
    Node[] extractTopKFrequentNodes(Node[] nodes, int k){
        return Arrays.copyOfRange(nodes, nodes.length-k, nodes.length);
    }
    void sortByFrequencyinDescendingOrder(Node[] knodes){
        Arrays.sort(knodes, (a,b) -> b.frequency - a.frequency);
    }
    int[] extractTopKFrequentElements(Node[] knodes){
        int[] elements = new int[knodes.length];
        int index=0;
        for(Node n: knodes){
            elements[index++] = n.element;
        }
        return elements;
    }    
    Map<Integer, Integer> getFrequencyMap(int[] nums){
        Map<Integer, Integer> map = new HashMap<>();
        for(Integer x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }
        return map;
    }
    Node[] buildNodesArrayFromMap(Map<Integer, Integer> map){
        Node[] nodes = new Node[map.keySet().size()];
        int index=0;
        for(Integer key: map.keySet()){
            nodes[index++] = new Node(key, map.get(key));
        }
        return nodes;
    }

}
