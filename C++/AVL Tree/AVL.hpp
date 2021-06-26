#ifndef __AVL_HPP_
#define __AVL_HPP_

#include <iostream>
#include <fstream>
#include <stack>

using namespace std;

class AVL {
private:
  class Node {
    Node *parent, *left, *right;
    int height;
    string element;

  public:
    Node(const string& e, Node *parent, Node *left, Node *right);
    
    Node*  getParent() const;
    Node*  getLeft() const;
    Node*  getRight() const;
    string getElement() const;
    int    getHeight() const;
  
    void setLeft(Node *);
    void setRight(Node *);
    void setParent(Node *);
    void setElement(string e);
    void setHeight(int h);
    bool isLeft() const;
    bool isRight() const;
    int  rightChildHeight() const;
    int  leftChildHeight() const;
    int  updateHeight();
    bool isBalanced();
  };
private:
  
  int   size;
  Node* root;
  
public:
    
    class Iterator {
      
    public:
      Iterator(Node* node); 
      Iterator& operator++();
      Iterator operator++(int a);
      string operator*(); 
      bool operator!=(Iterator iter);
      bool operator==(Iterator iter);
      stack<Node*> nodeStack;
      Node *currNode;
    };  
    
  Iterator begin() const;  
  Iterator end() const;
  
  static const int MAX_HEIGHT_DIFF = 1;
  AVL();
  AVL(const AVL& );
  bool contains(string e);
  bool add(string e);
  bool rmv(string e);
  void print2DotFile(char *filename);
  void pre_order(std::ostream& out);
  void insertNode(Node *node, string e);
  void updateNodesHeight(Node* node);
  void updateNodesBalance(Node* node);
  Node *rightRotate(Node *unBalancedNode);
  Node *leftRotate(Node *unBalancedNode);
  Node *childOfNode(Node *parent);
  Node *findSubstituteNode(Node *node);
  void graphTraversalPreorder(Node *currNode, string& line);
  void deleteTree(Node *currNode);

  friend std::ostream& operator<<(std::ostream& out, AVL& tree);  
  AVL& operator  =(const AVL& avl);
  AVL  operator  +(const AVL& avl);
  AVL& operator +=(const AVL& avl);
  AVL& operator +=(const string& e);
  AVL& operator -=(const string& e);
  AVL  operator  +(const string& e);
  AVL  operator  -(const string& e);
};

#endif
