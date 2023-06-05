package edu.duke.ece651.risc.shared.Entity;

public class Player {
  private Integer id;

  private String name;

  private String password;

  private Integer score;

  public Player(String name, String password) {
    this.name = name;
    this.password = password;
    this.score = 1500;
  }

  

  public Player(Integer id, String name, String password, Integer score) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.score = score;
  }



  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  
}
