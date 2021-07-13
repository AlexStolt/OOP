
#ifndef _GRAPH_UI_
#define _GRAPH_UI_



using namespace std;



template <typename T>
int graphUI() {
  
  string option, line;
  //int distance;
  bool digraph = false;
  
  cin >> option;
  if(!option.compare("digraph"))
    digraph = true;
  Graph<T> graph(digraph);
  
  while(true) {
    
    std::stringstream stream;
    cin >> option;
    
    if(!option.compare("av")) {
      getline(std::cin, line);
      stream << line;
      
      T vertex(stream);

      if(graph.add_vertex(vertex))
        std::cout << "av " << vertex << " OK\n";
      else
        std::cout << "av " << vertex << " NOK\n";
      
    }
    else if(!option.compare("rv")) {
      getline(std::cin, line);
      stream << line;
      T vertex(stream);
      
      if(graph.remove_vertex(vertex))
        cout << "rv " << vertex << " OK\n";
      else
        cout << "rv " << vertex << " NOK\n";

    }
    else if(!option.compare("ae")) {
      getline(std::cin, line);
      stream << line;

      T from_vertex(stream);
      T to_vertex(stream);
      
      int distance;
      stream >> distance;

      bool from_to = false;
      bool to_from = false;

      //Add Edge
      from_to = graph.add_edge(from_vertex, to_vertex, distance);
      if(!graph.directed()){
        to_from = graph.add_edge(to_vertex, from_vertex, distance);
      }
      
      //Output
      if(!graph.directed()){
        if(!from_to && !to_from)
          std::cout << "ae " << from_vertex << " " << to_vertex <<  " NOK" << endl;
        else
          std::cout << "ae " << from_vertex << " " << to_vertex <<  " OK" << endl;
      }
      else {
         if(!from_to)
           std::cout << "ae " << from_vertex << " " << to_vertex <<  " NOK" << endl;
         else
          std::cout << "ae " << from_vertex << " " << to_vertex <<  " OK" << endl;
      }
    }
    else if(!option.compare("re")) {

    }
    else if(!option.compare("dot")) {
      
    }
    else if(!option.compare("bfs")) {
      long unsigned int i = 0;

      getline(std::cin, line);
      stream << line;
      T vertex(stream);
      std::list<T> bfs_path = graph.bfs(vertex);

      std::cout << "\n----- BFS Traversal -----\n";
      
      
      for(auto iterator = bfs_path.begin(); iterator != bfs_path.end(); iterator++, i++){
        if(i < bfs_path.size() - 1)
          std::cout << *iterator << " -> ";
        else
          std::cout << *iterator;
      }


      std::cout << "\n-------------------------\n";
    }
    else if(!option.compare("dfs")) {
      long unsigned int i = 0;

      getline(std::cin, line);
      stream << line;
      T vertex(stream);
      std::list<T> dfs_path = graph.dfs(vertex);

      std::cout << "\n----- DFS Traversal -----\n";
      
      for(auto iterator = dfs_path.begin(); iterator != dfs_path.end(); iterator++, i++){
        if(i < dfs_path.size() - 1)
          std::cout << *iterator << " -> ";
        else
          std::cout << *iterator;
      }

      std::cout << "\n-------------------------\n";
    }
    else if(!option.compare("dijkstra")) {
      getline(std::cin, line);
      long unsigned int i = 0;

      stream << line;
      T from_vertex(stream);
      T to_vertex(stream);
      std::list <T> dijkstra_path = graph.dijkstra(from_vertex, to_vertex);

      std::cout << "Dijkstra (" << from_vertex << " - " << to_vertex <<"): ";
      
      for(auto iterator = dijkstra_path.begin(); iterator != dijkstra_path.end(); ++iterator, ++i){
        if(i < dijkstra_path.size() - 1)
          std::cout << *iterator << ", ";
        else
          std::cout << *iterator;
      }
      std::cout << endl;
    }
    else if(!option.compare("mst")) {
      std::list<GraphEdge<T>> mst_path = graph.mst();
      
      std::cout << "\n--- Min Spanning Tree ---\n";
      
      for(auto iterator = mst_path.begin(); iterator != mst_path.end(); iterator++){
        std::cout << *iterator << endl;
      }
      
      std::cout << "MST Cost: " << graph.mst_sum() << endl;
    }
    else if(!option.compare("q")) {
      cerr << "bye bye...\n";
      return 0;
    }
    else if(!option.compare("#")) {
      string line;
      getline(cin,line);
      cerr << "Skipping line: " << line << endl;
    }
    else {
      std::cout << "INPUT ERROR\n";
      return -1;
    }
  }
  return -1;  
}

#endif
