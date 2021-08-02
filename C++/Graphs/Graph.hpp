
#ifndef _GRAPH_HPP_ 
#define _GRAPH_HPP_

#include <iostream>
#include <list>
#include <vector>
#include <queue>
#include <stack>
#include <set>
#include <algorithm>
#include <ostream>
#include <fstream>

using namespace std;

//Edge Between Vertices on Graph
template<typename T>
struct GraphEdge {
  T from;
  T to;
  int distance;
  int to_vertex_priority; //Index of "To" Vertex on Adjacency List (Vertical Table)

  //Constructor
  GraphEdge(T from, T to, int distance, int to_vertex_priority = 0){
    this->from = from;
    this->to = to;
    this->distance = distance;
    this->to_vertex_priority = to_vertex_priority;
  }

  //Operator <
  bool operator < (const GraphEdge<T>& edge) const {
    return distance < edge.distance;
  }

  //Operator >
  bool operator > (const GraphEdge<T>& edge) const {
    return distance > edge.distance;
  }


  template<typename U>
  friend std::ostream& operator << (std::ostream& out, const GraphEdge<U>& e);
};

//Comparator to Sort List Based on the Vertex Priority
template<typename T>
struct vertex_priority {
  inline bool operator() (const GraphEdge<T>& left, const GraphEdge<T>& right){
    return left.to_vertex_priority < right.to_vertex_priority;
  }
};

//Output to Stream Operator
template<typename T>
std::ostream& operator << (std::ostream& out, const GraphEdge<T>& edge) {
  out << edge.from << " -- " << edge.to << " (" << edge.distance << ")";
  return out;
}

template <typename T>
class Graph {
  private:
    //Vertex Entry at Adjacency List    
    struct GraphVertex {
      T vertex_content; 
      std::vector<struct GraphEdge<T>> edges_list;

      //Constructors
      GraphVertex(){};
      GraphVertex(T content): vertex_content(content){};
      
      //Insert Edge on Edges List
      void insert_edge(struct GraphEdge<T> edge){
        edges_list.push_back(edge);
      }

      //Compare Vertex Content
      bool compare_vertex_content(T content) const {
        if(vertex_content != content){
          return false;
        }

        //Vertices Have the Same Content
        return true;
      }

      //Find Destination Edge from the Same Vertex
      bool edge_exists(T edge_content){
        for(long unsigned int i = 0; i < edges_list.size(); i++){
          if(edges_list[i].to != edge_content){
            continue;
          }
          else {
            return true;
          }
        }
        return false;
      }
    };

    //Heler Class to Store Dijkstra Node's Additional Info
    class DijkstraHelperTableEntry {
      public:
        T vertex_content;
        T previous_vertex; //Used to Reconstruct Path
        bool previous_vertex_exists; //Used to Check if the Previous Vertex Variable is Empty
        int distance_from_source;
        bool visited;

        //Constructor
        DijkstraHelperTableEntry(){
          visited = false;
          previous_vertex_exists = false;
        }
    };
    
    //Private Variables
    bool directed_graph;
    int mst_path_sum = 0;
    std::vector<struct GraphVertex> vertices_list;

    //Search Position of Vertex in the Vertical Adjacency List
    long int index_of_vertex(const T& info) const {
      for(long unsigned int i = 0; vertices_list.size(); i++){
        if(vertices_list[i].compare_vertex_content(info)){
          return i;
        }
      }

      return -1;
    }

  public:
    //Set Type of Graph
    Graph(bool directed = true){
      directed_graph = directed;
    }

    //Destructor to Deallocate Memory
    ~Graph(){
      //Nothing to Destroy
    }

    //Find a Certain Node in Graph 
    bool contains(const T& info){
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        if(vertices_list[i].compare_vertex_content(info)){
          return true;
        }
      }
      return false;
    }

    //Add Vertex on Graph
    bool add_vertex(const T& info){
      //Create a Vertex
      struct GraphVertex vertex(info);

      //Append Vertex on the Adjacency Table (Vertical Array) 
      vertices_list.push_back(vertex);
      
      return true;
    }

    //Remove Edges Connected to Vertex and the Vertex
    bool remove_vertex(const T& info){
      //Check Whether Vertex is in Graph
      if(!contains(info))
        return false;

      //Delete Edges Connected to Vertex and the Vertex Itself
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        //Delete Edges
        if(!vertices_list[i].compare_vertex_content(info)){
          if(!remove_edge(vertices_list[i].vertex_content, info))
            continue;
        }
        //Delete Vertex
        else {
          vertices_list.erase(vertices_list.begin() + i);
        }
      }

      return true;
    }

    //Add Edge on Graph
    bool add_edge(const T& from, const T& to, int distance){
      struct GraphEdge<T> *edge = NULL;
      int source_index, destination_index;

      //Find Vertices on Graph
      if(!contains(from) || !contains(to))
        return false;

      //Find Source Node
      source_index = index_of_vertex(from);

      //Check Whether Edge Already Exists
      if(vertices_list[source_index].edge_exists(to))
        return false;
      
      //Find Destination Node
      destination_index = index_of_vertex(to);

      //Create Edge
      edge = new GraphEdge<T>(from, to, distance, destination_index);

      //Push Back the Edge to the Edges List (Horizontal List)
      vertices_list[source_index].insert_edge(*edge);
      
      //Sort Edges Based on their Vertex Entry Priority
      std::sort(vertices_list[source_index].edges_list.begin(), vertices_list[source_index].edges_list.end(), vertex_priority<T>());

      //Delete Edge Pointer
      delete edge;

      return true;
    }

    //Remove Edge
    bool remove_edge(const T& from, const T& to){
      int source_index;
      
      //Check Source Vertex is in Graph
      if(!contains(from))
        return false;

      //Find Source Vertex
      source_index = index_of_vertex(from);
      
      //Find and Delete Edge
      for(long unsigned int i = 0; i < vertices_list[source_index].edges_list.size(); i++){
        if(vertices_list[source_index].edges_list[i].to != to)
          continue;
        else {
          vertices_list[source_index].edges_list.erase(vertices_list[source_index].edges_list.begin() + i);
          return true;
        }
      }

      return false;
    }

    //Implementation of DFS Algorithm
    std::list<T> dfs(const T& info) const {
      std::stack<struct GraphVertex> stack;
      struct GraphVertex vertex, edge_destination_vertex;
      std::list <T> dfs_path;
      bool *visit_set;

      
      //Allocate Memory for Set
      visit_set = new bool[vertices_list.size()];

      //Initialize Set
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        visit_set[i] = false;
      }

      //Starting Vertex
      vertex = vertices_list[index_of_vertex(info)];
      stack.push(vertex);

      //Stack Souldn't be Empty
      while (!stack.empty()){
        //Get Top Element of LIFO
        vertex = stack.top();

        //Remove Top Element from Stack  
        stack.pop();

        //Mark Vertex as Visited
        if(!visit_set[index_of_vertex(vertex.vertex_content)])
          visit_set[index_of_vertex(vertex.vertex_content)] = true;
        else
          continue;
        
        //Add Vertex to Path
        dfs_path.push_back(vertex.vertex_content);

        //Push Neighbour Edges to Stack
        for(long int i = vertex.edges_list.size() - 1; i >= 0; i--){

          //Get Destination Vertices
          edge_destination_vertex = vertices_list[index_of_vertex(vertex.edges_list[i].to)]; 

          if(visit_set[index_of_vertex(edge_destination_vertex.vertex_content)]){
            continue;
          }
          else {
            //Add Vertex to the Visit Stack
            stack.push(edge_destination_vertex);
          }
        }
      }

      delete[] visit_set;

      return dfs_path;
    }
    
    //Implementation of BFS Algorithm
    std::list<T> bfs(const T& info) const {
      std::queue<struct GraphVertex> queue;
      struct GraphVertex vertex, edge_destination_vertex;
      std::list <T> bfs_path;
      bool *visit_set;

      
      //Allocate Memory for Set
      visit_set = new bool[vertices_list.size()];

      //Initialize Set
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        visit_set[i] = false;
      }

      //Starting Vertex
      vertex = vertices_list[index_of_vertex(info)];
      queue.push(vertex);

      //Queue Souldn't be Empty
      while (!queue.empty()){
        //Get Top Element of FIFO
        vertex = queue.front();

        //Add Vertex to Path
        bfs_path.push_back(vertex.vertex_content);

        //Mark Vertex as Visited
        visit_set[index_of_vertex(vertex.vertex_content)] = true;

        //Add Edges to Visit Queue
        for(long unsigned int i = 0; i < vertex.edges_list.size(); i++){

          //Get Destination Vertices
          edge_destination_vertex = vertices_list[index_of_vertex(vertex.edges_list[i].to)]; 

          if(visit_set[index_of_vertex(edge_destination_vertex.vertex_content)]){
            continue;
          }
          else {
            //Mark Vertex as Visited
            visit_set[index_of_vertex(edge_destination_vertex.vertex_content)] = true;

            //Add Vertex to the Visit Queue
            queue.push(edge_destination_vertex);
          }
        }

        //Remove Element Visit Queue  
        queue.pop();
      }

      delete[] visit_set;

      return bfs_path;
    }

    //Implementation of MST
    std::list<GraphEdge<T>> mst(){
      std::list<GraphEdge<T>> mst_path; //Path of MST Traversal
      std::vector<GraphEdge<T>> edges_set;
      struct GraphVertex vertex;
      GraphEdge<T> *sorted_edge;
      int index_of_shortest_distance_edge, shortest_distance_edge;
      int from_index, to_index;
      bool *visit_set; 

      //Directed or Empty Graphs Return an Empty Path
      if(directed_graph || !vertices_list.size())
        return mst_path;

      //Create a Visit Set
      visit_set = new bool[vertices_list.size()];

      //Initialize Visit Set
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        visit_set[i] = false;
      }

      //Starting Vertex
      vertex = vertices_list[0];

      //Create a Set of Edges
      for(long unsigned i = 0; i < vertex.edges_list.size(); i++){
        edges_set.push_back(vertex.edges_list[i]);
      }


      //MST Algorithm
      while (true){
        index_of_shortest_distance_edge = -1;
        shortest_distance_edge = INT32_MAX;
        from_index = -1;
        to_index = -1;
        sorted_edge = NULL;

        //Find Shortest Distance Edge
        for(long unsigned int i = 0; i < edges_set.size(); i++){
          //Search on Visited Vertex Set
          if(!visit_set[index_of_vertex(edges_set[i].to)]){
            if(shortest_distance_edge > edges_set[i].distance){
              shortest_distance_edge = edges_set[i].distance; //Set Shortest Distance
              index_of_shortest_distance_edge = i;  //A "Pointer" to the Position of the Shortest Distance
            }
          }
        }

        //********** EXIT CONDITION: No Shortest Distance Edges Found **********//
        if(index_of_shortest_distance_edge < 0)
          break;
        

        //Create an Edge (Based on the Priority of the Destination Vertex)
        from_index = index_of_vertex(edges_set[index_of_shortest_distance_edge].from);
        to_index = index_of_vertex(edges_set[index_of_shortest_distance_edge].to);
        if(from_index < to_index){
          sorted_edge = new GraphEdge<T>(edges_set[index_of_shortest_distance_edge].from,
                                         edges_set[index_of_shortest_distance_edge].to, 
                                         edges_set[index_of_shortest_distance_edge].distance
                                        );
        }
        else {
          sorted_edge = new GraphEdge<T>(edges_set[index_of_shortest_distance_edge].to,
                                         edges_set[index_of_shortest_distance_edge].from, 
                                         edges_set[index_of_shortest_distance_edge].distance
                                        );
        }

        //Construct Path
        mst_path.push_back(*sorted_edge);
        mst_path_sum = mst_path_sum + sorted_edge->distance;

        //Set Visited Nodes
        visit_set[index_of_vertex(sorted_edge->from)] = true;
        visit_set[index_of_vertex(sorted_edge->to)] = true;

        //De-Allocate Memory of Sorted Edge
        delete sorted_edge;

        //Set Vertex to Visit
        vertex = vertices_list[index_of_vertex(edges_set[index_of_shortest_distance_edge].to)];

        //Erase Edge from Set
        edges_set.erase(edges_set.begin() + index_of_shortest_distance_edge);

        //Get Not Already Visited Vertex Edges
        for(long unsigned int i = 0; i < vertex.edges_list.size(); i++){
          if(!visit_set[index_of_vertex(vertex.edges_list[i].to)])
            edges_set.push_back(vertex.edges_list[i]);
        }
      }

      //Sort Based on Distance (Overloaded Operators: "<" or ">" are Used)
      mst_path.sort();

      delete[] visit_set;

      return mst_path;
    }
    
    //Create a Dot File of the Graph
    void print2DotFile(const char *filename) const {
      ofstream dot_file;

      //Empty Graph
      if(!vertices_list.size()){
        return;
      }

      //Open File to Write
      dot_file.open(filename);

      if(!directed_graph){
        dot_file << "graph ";
      }
      else {
        dot_file << "digraph ";
      }

      dot_file << "{" << endl;
    
      //Write Edges on File
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        for(long unsigned int j = 0; j < vertices_list[i].edges_list.size(); j++){
          dot_file << "    " << vertices_list[i].edges_list[j].from << " -- " << vertices_list[i].edges_list[j].to << endl;
        }
      }

      dot_file << "}" << endl;

      //Close File
      dot_file.close();
    }

    //Implementation of Dijkstra's Algorithm
    std::list<T> dijkstra(const T& from, const T& to) {
      DijkstraHelperTableEntry *dijkstra_vertex_vector;
      struct GraphVertex vertex_to_visit;
      std::list<T> dijkstra_path;
      int shortest_distance_vertex;
      long int helper_table_index = -1; //Used as a Pointer on the Dijkstra's Helper Table
      long int shortest_distance_index = -1;

      //Path Reconstruction
      long int path_reconstructor_index = -1;
      long int source_index = -1;
      long int drain_index = -1;

      //Create Dijkstra's Helper Vector
      dijkstra_vertex_vector = new DijkstraHelperTableEntry[vertices_list.size()];

      //Initialize Dijkstra's Helper Vector
      for(long unsigned int i = 0; i < vertices_list.size(); i++){
        dijkstra_vertex_vector[i].vertex_content = vertices_list[i].vertex_content;
        
        if(dijkstra_vertex_vector[i].vertex_content != from){
          dijkstra_vertex_vector[i].distance_from_source = INT32_MAX;
        }
        else {
          dijkstra_vertex_vector[i].distance_from_source = 0;
        }
      }

      //Dijkstra's Algorithm Implementation
      while(true){
        shortest_distance_index = -1;
        helper_table_index = -1;
        shortest_distance_vertex = INT32_MAX;

        //Find Shortest Distance Vertex
        for(long unsigned int i = 0; i < vertices_list.size(); i++){
          //Find Shortest Distance (Not Visited) Node
          if(!dijkstra_vertex_vector[i].visited){
            if(dijkstra_vertex_vector[i].distance_from_source < shortest_distance_vertex){ 
              shortest_distance_vertex = dijkstra_vertex_vector[i].distance_from_source;
              shortest_distance_index = i;
            }
          }
        }

        //********** EXIT CONDITION: Visited All Available Nodes **********//
        if(shortest_distance_index < 0)
          break;

        //Visit Vertex
        dijkstra_vertex_vector[shortest_distance_index].visited = true;

        //Get Vertex to Visit from Vertices List
        vertex_to_visit = vertices_list[index_of_vertex(dijkstra_vertex_vector[shortest_distance_index].vertex_content)];

        //Distance Update on the Helper Table Based on the Edges
        for(long unsigned int i = 0; i < vertex_to_visit.edges_list.size(); i++){
          //Get Index of Destination Vertex from Edge
          helper_table_index = index_of_vertex(vertex_to_visit.edges_list[i].to);

          //Update Helper Table
          if(dijkstra_vertex_vector[helper_table_index].distance_from_source > dijkstra_vertex_vector[shortest_distance_index].distance_from_source + vertex_to_visit.edges_list[i].distance){
            
            dijkstra_vertex_vector[helper_table_index].distance_from_source = dijkstra_vertex_vector[shortest_distance_index].distance_from_source + vertex_to_visit.edges_list[i].distance;
            dijkstra_vertex_vector[helper_table_index].previous_vertex = vertex_to_visit.vertex_content;
            dijkstra_vertex_vector[helper_table_index].previous_vertex_exists = true;
          }
        }
      }

      //Source Vertex
      source_index = index_of_vertex(from);

      //Destination Vertex
      drain_index = index_of_vertex(to);
      
      //Back Trace Path
      path_reconstructor_index = drain_index;


      //Reconstruct Path
      while (path_reconstructor_index != source_index){
        if(!dijkstra_vertex_vector[path_reconstructor_index].previous_vertex_exists){
          break;
        }

        //Add Vertex to Path
        dijkstra_path.push_front(dijkstra_vertex_vector[path_reconstructor_index].vertex_content);
        
        //Go Towards Source
        path_reconstructor_index = index_of_vertex(dijkstra_vertex_vector[path_reconstructor_index].previous_vertex);
      }

      //Push Source Node
      if(path_reconstructor_index == source_index){
        dijkstra_path.push_front(dijkstra_vertex_vector[source_index].vertex_content);
      }

      delete[] dijkstra_vertex_vector;

      return dijkstra_path;
    }

    //Return Type of Graph
    bool directed(){
      return directed_graph;
    }

    //Return the MST Path Sum
    int mst_sum(){
      return mst_path_sum;
    }
};

#endif
