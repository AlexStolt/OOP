#ifndef _GRAPH_HPP_ 
#define _GRAPH_HPP_


#include <iostream>
#include <list>
#include <vector>
#include <queue>
#include <stack>
#include <set>
#include <algorithm>

using namespace std;

template<typename T>
struct Edge {
  T from;
  T to;
  int dist;
  int vertex_priority;
  Edge(T from, T to, int distance, int vertex_priority): from(from), to(to), dist(distance), vertex_priority(vertex_priority) {};
  bool operator < (const Edge<T>& edge) const {
    return dist < edge.dist;
  }
  bool operator > (const Edge<T>& edge) const {
    return dist > edge.dist;
  }

  template<typename U>
  friend std::ostream& operator<<(std::ostream& out, const Edge<U>& e);
};

template<typename T>
struct DijkstraEntry {
  T vertex;
  int distance;
  bool visited;
  T previous;
  bool previous_exists;
};

template<typename T>
struct priority_comparator {
  inline bool operator() (const Edge<T> *left, const Edge<T> *right){
    return (left->vertex_priority < right->vertex_priority);
  }
};

template<typename T>
struct distance_comparator {
  inline bool operator() (const Edge<T>& left, const Edge<T>& right){
    return (left.dist < right.dist);
  }
};

template<typename T>
std::ostream& operator<<(std::ostream& out, const Edge<T>& e) {
  out << e.from << " -- " << e.to << " (" << e.dist << ")";
  return out;
}

template <typename T>
class Vertex {
  public:
    T content; 
    std::vector<struct Edge<T>*> edges_array;

    //Constructor
    Vertex(T content){
      this->content = content;
    }

    //Get Content Info
    T get_content(){
      return content;
    }

    //Compare Content of Vertex with Info
    bool compare(T info){
      if(content != info)
        return false;
      
      return true;
    }

    //Methods
    void add_edge(struct Edge<T>* edge){
      edges_array.push_back(edge);
    }

    struct Edge<T> *find_edge_destination(T to){
      for(long unsigned int i = 0; i < edges_array.size(); i++){
        if(edges_array[i]->to == to){
          return edges_array[i];
        }
      }
      return NULL;
    }
};


template <typename T>
class Graph {
  private:
    bool _directed;
    int vertices = 0;
    std::vector<Vertex<T>*> vertices_array;

    //Find and Return a Pointer to a Vertex with Content Info
    Vertex<T> *get_vertex(const T& info) const {
      for(long unsigned int i = 0; i < vertices_array.size(); i++){
        if(vertices_array[i]->compare(info))
          return vertices_array[i];
      }
      return NULL;
    }

    //Search Index of Vertex
    long int vertex_index(const T& info) const {
      for(long unsigned int i = 0; vertices_array.size(); i++){
        if(vertices_array[i]->compare(info)){
          return i;
        }
      }

      return -1;
    } 

  public:
    int sum = 0;

    //Set if Graph is Directed
    Graph(bool directed = true){
      _directed = directed;
    }
    
    //Return True if the Graph is Directed
    bool is_directed(){
      return _directed;
    }

    //Search Graph for the a Certain Node
    bool contains(const T& info){
      bool found = false;
      for(long unsigned int i = 0; i < vertices_array.size(); i++){
        if(vertices_array[i]->compare(info)){
          found = true;
        }
      }
      return found;
    }

    //Create and Append to an Array of Vertices
    bool addVtx(const T& info){
      //Create a Verex on the Adjacency "Table" (1D Array) 
      Vertex<T> *vertex = new Vertex<T>(info);

      vertices_array.push_back(vertex);
      
      return true;
    }

    bool rmvVtx(const T& info){
      Vertex<T> *current_vertex;
      if(!(current_vertex = get_vertex(info)))
        return false;

      //Delete Edges that Connected to this Vertex
      for(long unsigned int i = 0; i < vertices_array.size(); i++){
        //Delete Vertex on Array of Verices 
        if(vertices_array[i]->compare(info)){
          vertices_array.erase(vertices_array.begin() + i);
        }
        
        //Delete Edges that Contain the Vertex
        for(long unsigned int j = 0; j < vertices_array[i]->edges_array.size(); j++){
          if(vertices_array[i]->edges_array[j]->to == info){
            vertices_array[i]->edges_array.erase(vertices_array[i]->edges_array.begin() + j);
          }
        }
      }

      return true;    
    }
    
    //Add Edge Between Two Nodes on Graph
    bool addEdg(const T& from, const T& to, int distance){
      Vertex<T> *from_vertex_ptr = NULL;
      struct Edge<T> *edge = NULL;
      int index;

      //Vertices must be on Graph
      if(!contains(from) || !contains(to))
        return false;

      //Connect From ---> To
      from_vertex_ptr = get_vertex(from);
      if(!from_vertex_ptr)
        return false;

      //Check if Edge Already Exists
      if(from_vertex_ptr->find_edge_destination(to) != NULL)
        return false;

      //Get Index of Vertex
      index = vertex_index(to);
      if(index < 0)
        return false;

      //Create Edge
      edge = new Edge<T>(from, to, distance, index);

      //Add Edge on Graph
      from_vertex_ptr->add_edge(edge);
      
      //Sort Edges
      std::sort(from_vertex_ptr->edges_array.begin(), from_vertex_ptr->edges_array.end(), priority_comparator<T>());

      
      return true;
    }
    
    void print_graph() const {
      for(int i = 0; i < vertices_array.size(); i++){
        cout << vertices_array[i]->get_content() << " || ";
        for(int j = 0; j < vertices_array[i]->edges_array.size(); j++){
          cout << vertices_array[i]->edges_array[j]->to << " ";
        }
        cout << endl;
      }
    }


    //Remove Edge from Graph
    bool rmvEdg(const T& from, const T& to);



    std::list<T> dfs(const T& info) const {
      std::list <T> path;
      std::stack<Vertex<T>*> dfs_stack;  //Queue to Hold Graph Path
      std::set<T> visited_set; //Set to Add Visited Nodes
      Vertex<T> *current_vertex = NULL, *edge_vertex;
      

      current_vertex = get_vertex(info);
      dfs_stack.push(current_vertex);

      while (!dfs_stack.empty()){
        current_vertex = dfs_stack.top();
        
        
        //Remove One Element from Stack  
        dfs_stack.pop();

        //Add Vertex to Visited Set
        if(visited_set.find(current_vertex->get_content()) == visited_set.end()){ //if(!visited)
          visited_set.insert(current_vertex->get_content());
        }
        else {
          continue;
        }

        path.push_back(current_vertex->get_content());

        //Add Edges to Stack
        for(long int i = current_vertex->edges_array.size() - 1; i > -1; i--){
          edge_vertex = get_vertex(current_vertex->edges_array[i]->to);
          
          if(visited_set.find(edge_vertex->get_content()) != visited_set.end()){ //if(visited == true)
            continue; //Node Already Visited
          }
          else {
            //Schedule Vertex for a Future Visit
            dfs_stack.push(edge_vertex);
          }
        }        
      }

      return path;
    }

    std::list<T> bfs(const T& info) const {
      std::list <T> path;
      std::queue<Vertex<T>*> bfs_queue;  //Queue to Hold Graph Path
      std::set<T> visited_set; //Set to Add Visited Nodes
      Vertex<T> *current_vertex = NULL, *edge_vertex;
      
      //print_graph();

      //First Elelment of the Queue
      current_vertex = get_vertex(info);
      bfs_queue.push(current_vertex);

      while (!bfs_queue.empty()){
        current_vertex = bfs_queue.front();
        path.push_back(current_vertex->get_content()); //Create Path of Visited Nodes
        
        //Add Element to Visited Set
        visited_set.insert(current_vertex->get_content());

        //Search Edges
        for(long unsigned int i = 0; i < current_vertex->edges_array.size(); i++){
          edge_vertex = get_vertex(current_vertex->edges_array[i]->to); //Get Destination Nodes

          if(visited_set.find(edge_vertex->get_content()) != visited_set.end()){
            continue; //Node Already Visited
          }
          else {
            //Add Edge to the Visited Set
            visited_set.insert(edge_vertex->get_content());
            //Schedule Vertex for a Future Visit
            bfs_queue.push(edge_vertex);
          }

        }

        //Remove One Element from Queue  
        bfs_queue.pop();
      }
      
      return path;
    }

    std::list<Edge<T>> mst(){
      std::list<Edge<T>> mst_list; //Return List of Edges
      std::vector<Edge<T>> edges_vector;
      Vertex<T> *current_vertex = NULL;
      Edge<T> *edge;
      bool *visited = new bool[mst_list.size()];
      int shortest_distance_index, shortest_distance;
      
      //Directed Graph -> Return Empty List
      if(is_directed() || !vertices_array.size())
        return mst_list;
      else 
        current_vertex = vertices_array[0];

      //Initialize Visited Array
      for(long unsigned int i = 0; i < mst_list.size(); i++){
        visited[i] = false;
      }

      //Add Entry Node's Edges in a Set
      for(long unsigned int i = 0; i < vertices_array[0]->edges_array.size(); i++){
        edges_vector.push_back(*current_vertex->edges_array[i]);
      }
      
      while (edges_vector.size() > 0){
        shortest_distance = INT32_MAX;
        shortest_distance_index = -1;
        edge = NULL;

        for(long unsigned int i = 0; i < edges_vector.size(); i++){
          if(!visited[vertex_index(edges_vector[i].to)]){
              if(shortest_distance > edges_vector[i].dist){
                shortest_distance_index = i; //Index
                shortest_distance = edges_vector[i].dist; //Distance
            }
          }
        }
        if(shortest_distance_index < 0)
          break;

        //Create Edge
        if(vertex_index(edges_vector[shortest_distance_index].from) < vertex_index(edges_vector[shortest_distance_index].to))
          edge = new Edge<T>(edges_vector[shortest_distance_index].from, edges_vector[shortest_distance_index].to, edges_vector[shortest_distance_index].dist, 0);
        else
          edge = new Edge<T>(edges_vector[shortest_distance_index].to, edges_vector[shortest_distance_index].from, edges_vector[shortest_distance_index].dist, 0);
        
        //Add to MST List
        mst_list.push_back(*edge);
        sum = sum + edge->dist;
        
        //Get Vertex
        current_vertex = get_vertex(edges_vector[shortest_distance_index].to);

        //Set Current Vertex as Visited
        visited[vertex_index(edges_vector[shortest_distance_index].from)] = true;
        visited[vertex_index(edges_vector[shortest_distance_index].to)] = true;

        //Remove Shortest Distance Edge from Edges Vector
        edges_vector.erase(edges_vector.begin() + shortest_distance_index);

        //Get Vertex Edges
        for(long unsigned int i = 0; i < current_vertex->edges_array.size(); i++){
          if(!visited[vertex_index(current_vertex->edges_array[i]->to)])
            edges_vector.push_back(*current_vertex->edges_array[i]);
        }
      }

      mst_list.sort();
      
      return mst_list;
    }
    
    bool print2DotFile(const char *filename) const;
    
    
    std::list<T> dijkstra(const T& from, const T& to){
      DijkstraEntry<T> *dijkstra_distance_vector, *shortest_dijkstra_entry;
      Vertex<T> *visit_vertex = NULL;
      std::list<T> path;
      int shortest_distance;
      int index, backtrace_index, source_index;

      //Create a Helper Array 
      dijkstra_distance_vector = new DijkstraEntry<T>[vertices_array.size()];

      //Initialize Dijkstra Vector
      for(long unsigned int i = 0; i < vertices_array.size(); i++){
        dijkstra_distance_vector[i].vertex = vertices_array[i]->get_content();
        
        if(dijkstra_distance_vector[i].vertex != from){
          dijkstra_distance_vector[i].distance = INT32_MAX;
          dijkstra_distance_vector[i].visited = false;
        }
        else {
          dijkstra_distance_vector[i].distance = 0;
          dijkstra_distance_vector[i].visited = false;
        }
        dijkstra_distance_vector[i].previous_exists = false;
      }


      //Dijkstra's Algorithm
      while(true){
        visit_vertex = NULL;
        index = 0;
        shortest_dijkstra_entry = NULL;
        shortest_distance = INT32_MAX - 1;

        for(long unsigned int i = 0; i < vertices_array.size(); i++){
          //Find Shortest Distance Node that is NOT Visited
          if(!dijkstra_distance_vector[i].visited){
            if(dijkstra_distance_vector[i].distance < shortest_distance){
              shortest_dijkstra_entry = dijkstra_distance_vector + i; 
              shortest_distance = dijkstra_distance_vector[i].distance;
            }
          }
        }

        //All Nodes are Visited
        if(!shortest_dijkstra_entry){
          break;
        }

        //Mark this Node as Visited
        shortest_dijkstra_entry->visited = true;

        //Vertex in Adjacency List
        visit_vertex = get_vertex(shortest_dijkstra_entry->vertex);

        //Update Edges Distance
        for(long unsigned int i = 0; i < visit_vertex->edges_array.size(); i++){
          //Get Index of Vertex
          index = vertex_index(visit_vertex->edges_array[i]->to);

          if(dijkstra_distance_vector[index].distance > shortest_dijkstra_entry->distance + visit_vertex->edges_array[i]->dist){
            dijkstra_distance_vector[index].distance = shortest_dijkstra_entry->distance + visit_vertex->edges_array[i]->dist;
            dijkstra_distance_vector[index].previous = visit_vertex->content;
            dijkstra_distance_vector[index].previous_exists = true;
          }
        }
      }
      
      //Rebuild Path
      
      //Get Index of Destination Node
      backtrace_index = vertex_index(to);
      if(backtrace_index < 0)
        return path;
      
      source_index = vertex_index(from);
      if(source_index < 0)
        return path;

      //Reconstruct Path
      while (backtrace_index != source_index){
        if(!dijkstra_distance_vector[backtrace_index].previous_exists)
          break;

        path.push_front(dijkstra_distance_vector[backtrace_index].vertex);
        backtrace_index = vertex_index(dijkstra_distance_vector[backtrace_index].previous);

        if(backtrace_index < 0)
          break;
      }

      //Push Origin Node
      if(backtrace_index == source_index){
        path.push_front(dijkstra_distance_vector[source_index].vertex);
      }

      delete[] dijkstra_distance_vector;

      return path;
    }
};

#endif