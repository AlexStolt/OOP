
#ifndef _GRAPH_UI_
#define _GRAPH_UI_

#include <iostream>
using namespace std;

template <typename T>
int graphUI() {
  
  string option, line;
  //int distance;
  bool digraph = false;
  
  cin >> option;
  if(!option.compare("digraph"))
    digraph = true;
  Graph<T> g(digraph);
  
  while(true) {
    
    std::stringstream stream;
    cin >> option;
    
    if(!option.compare("av")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.addVtx(vtx))
        cout << "av " << vtx << " OK\n";
      else
        cout << "av " << vtx << " NOK\n";
    }
    else if(!option.compare("rv")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.rmvVtx(vtx))
        cout << "rv " << vtx << " OK\n";
      else
        cout << "rv " << vtx << " NOK\n";
    }
    else if(!option.compare("ae")) {
      bool edges[2] = {false, false};

      getline(std::cin, line);
      stream << line;
      
      T from(stream);
      T to(stream);
      
      int distance;
      stream >> distance;

      //Add Edge Between Nodes
      edges[0] = g.addEdg(from, to, distance);
      if(!g.is_directed()){
        edges[1] = g.addEdg(to, from, distance);
      }

      if((g.is_directed() && !edges[0]) || (!g.is_directed() && !edges[0] && !edges[1])){
        cout << "ae " << from << " " << to <<  " NOK" << endl;
      }
      else {
        cout << "ae " << from << " " << to <<  " OK" << endl;
      }
    }
    else if(!option.compare("re")) {

    }
    else if(!option.compare("dot")) {
      
    }
    else if(!option.compare("bfs")) {
      getline(std::cin, line);
      stream << line;
      T node(stream);

      cout << "\n----- BFS Traversal -----\n";
      std::list <T> path = g.bfs(node);

      long unsigned int i = 0;
      for(auto current = path.begin(); current != path.end(); ++current, ++i){
        if(i < path.size() - 1)
          std::cout << *current << " -> ";
        else
          std::cout << *current;
      }

      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dfs")) {
      getline(std::cin, line);
      stream << line;
      T node(stream);
      cout << "\n----- DFS Traversal -----\n";
      std::list <T> path = g.dfs(node);

      long unsigned int i = 0;
      for(auto current = path.begin(); current != path.end(); ++current, ++i){
        if(i < path.size() - 1)
          std::cout << *current << " -> ";
        else
          std::cout << *current;
      }
      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dijkstra")) {
      getline(std::cin, line);
      stream << line;
      T from(stream);
      T to(stream);

      std::list <T> path = g.dijkstra(from, to);

      cout << "Dijkstra (" << from << " - " << to <<"): ";
      long unsigned int i = 0;
      for(auto current = path.begin(); current != path.end(); ++current, ++i){
        if(i < path.size() - 1)
          std::cout << *current << ", ";
        else
          std::cout << *current;
      }
      std::cout << endl;
    }
    else if(!option.compare("mst")) {

      cout << "\n--- Min Spanning Tree ---\n";
      std::list<Edge<T>> mst_list = g.mst();
      
      for(auto current = mst_list.begin(); current != mst_list.end(); ++current){
        std::cout << *current << endl;
      }
      cout << "MST Cost: " << g.sum << endl;
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
      cout << "INPUT ERROR\n";
      return -1;
    }
  }
  return -1;  
}


#endif
