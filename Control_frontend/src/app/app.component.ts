import { Component, OnInit } from '@angular/core';
import * as joint from 'jointjs';
import * as Backbone from 'backbone';
import * as $ from 'jquery';
import * as _ from 'lodash';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Control_frontend';
  graph!: joint.dia.Graph;
  nodeID: number = 0;
  linking: boolean = false;
  selecting: boolean = false;
  source!: joint.dia.Cell;
  target!: joint.dia.Cell;
  selected!: joint.dia.Cell | joint.shapes.standard.Link;
  nodeOrEdge: boolean = true;

  constructor(private http: HttpClient){}
  
  ngOnInit(): void {}

  ngAfterViewInit() {
    this.graph = new joint.dia.Graph;
    const paper = new joint.dia.Paper({
      el: $('#myDiagram'),
      model: this.graph,
      width: 1200,
      height: 620,
      gridSize: 10
    });

    paper.on('cell:pointerdown', (cellView, evt) => {
      if(this.linking) {
        if (!this.source) {
          this.source = cellView.model;
          this.source.attr('circle/stroke', 'green');
        } else if (!this.target) {
          this.target = cellView.model;
          this.source.attr('circle/stroke', 'black');
          const button = document.getElementById('link');
          if(button)
            button.style.backgroundColor = '#547f98';
          this.connectNodes(this.source, this.target, 0);
          this.source = this.graph.getCell('') || null;
          this.target = this.graph.getCell('') || null;
        }
      }else if(this.selecting){
        if(!this.selected){
          this.selected = cellView.model;
          if(this.selected instanceof joint.shapes.standard.Link){
            this.nodeOrEdge = false;
            let source = this.selected.getSourceCell();
            let target = this.selected.getTargetCell();
            source?.attr('circle/stroke', 'orange');
            target?.attr('circle/stroke', 'orange');
          }else{
            this.nodeOrEdge = true;
            this.selected.attr('circle/stroke', 'orange');
          }
          // const button = document.getElementById('sel');
          // if(button)
          //   button.style.backgroundColor = '#568bac';
          // this.selecting = false;
        }else if(this.selected){
          if(this.selected instanceof joint.shapes.standard.Link){
            let source = this.selected.getSourceCell();
            let target = this.selected.getTargetCell();
            source?.attr('circle/stroke', 'black');
            target?.attr('circle/stroke', 'black');
          }else{
            this.selected.attr('circle/stroke', 'black');
          }
          if(cellView.model == this.selected){
            this.selected = this.graph.getCell('') || null;
            this.nodeOrEdge = true;
            return;
          }
          this.selected = cellView.model;
          if(this.selected instanceof joint.shapes.standard.Link){
            this.nodeOrEdge = false;
            let source = this.selected.getSourceCell();
            let target = this.selected.getTargetCell();
            source?.attr('circle/stroke', 'orange');
            target?.attr('circle/stroke', 'orange');
          }else{
            this.nodeOrEdge = true;
            this.selected.attr('circle/stroke', 'orange');
          }
          // const button = document.getElementById('sel');
          // if(button)
          //   button.style.backgroundColor = '#568bac';
          // this.selecting = false;
        }
      }
    });

    document.getElementById('addNode')?.addEventListener('click', () => {
      var x = 50;
      var y = 50;
      this.createNode(x, y);
    })
    document.getElementById('link')?.addEventListener('click', () => {
      this.startLinking();
    })
    document.getElementById('sel')?.addEventListener('click', () => {
      this.startSelecting();
    })
    document.getElementById('setWeight')?.addEventListener('click', () => {
      this.updateWeight();
    })
    document.getElementById('del')?.addEventListener('click', () => {
      this.deleteLink();
    })
    document.getElementById('submit')?.addEventListener('click', () => {
      this.submit();
    })
  }

  createNode(x: number, y: number) {
    const circle = new joint.shapes.basic.Circle({
      id: `${this.nodeID}`,
      position: {x, y},
      size: { width: 50, height: 50 },
      attrs: {
        circle: {
          fill: 'transparent',
          strokeWidth: 5
        },
        text: {
          text: `${this.nodeID++}`,
          fill: 'black'
        }
      }
    });
    this.graph.addCell(circle);
  }

  connectNodes(source: joint.dia.Cell, target: joint.dia.Cell, weight: number) {
    const link = new joint.shapes.standard.Link({
      source: { id: source.id },
      target: { id: target.id },
      labels: [
        {
          position: 0.5,
          attrs: {
            text: {
              text: weight.toString(),
              fill: 'black'
            }
          }
        }
      ]
    });
    link.prop('weight', weight);
    link.router('metro');
    this.graph.addCell(link);
    this.linking = false;
  }

  startLinking(){
    if(this.linking){
      this.linking = false;
      this.source = this.graph.getCell('') || null;
      const button = document.getElementById('link');
      if(button)
        button.style.backgroundColor = '#547f98';
      return
    }
    if(this.selecting){
      this.selecting = false;
      const button = document.getElementById('sel');
      if(button)
        button.style.backgroundColor = '#568bac';
    }
    this.linking = true;
    const button = document.getElementById('link');
    if(button)
      button.style.backgroundColor = 'lime';
  }

  startSelecting(){
    if(this.selecting){
      this.selecting = false;
      const button = document.getElementById('sel');
      if(button)
        button.style.backgroundColor = '#568bac';
      return
    }
    if(this.linking){
      this.linking = false;
      this.source.attr('circle/stroke', 'black');
      this.source = this.graph.getCell('') || null;
      const button = document.getElementById('link');
      if(button)
        button.style.backgroundColor = '#547f98';
    }
    this.selecting = true;
    const button = document.getElementById('sel');
    if(button)
      button.style.backgroundColor = 'lime';
  }

  updateWeight(){
    const input: HTMLInputElement = document.getElementById('newWeight') as HTMLInputElement;
    const newWeight = input.value;
    if(this.selected){
      this.selected.attr('text/text', newWeight);
      this.selected.prop('weight', newWeight);
    }
    input.value = "";
  }

  deleteLink(){
    if(this.selected instanceof joint.shapes.standard.Link){
      let source = this.selected.getSourceCell();
      let target = this.selected.getTargetCell();
      source?.attr('circle/stroke', 'black');
      target?.attr('circle/stroke', 'black');
      this.selected.remove();
      this.selected = this.graph.getCell('') || null;
      this.nodeOrEdge = true;
    }
  }

  submit(){
    let edges = this.graph.getLinks()
    const adjList: [number, number][][] = new Array(this.nodeID).fill(null).map(() => []);
    for(let edge of edges){
      let source = edge.getSourceElement();
      let target = edge.getTargetElement();
      let weight = parseFloat(edge.prop('weight'));
      if(source !== null && target !== null){
        adjList[parseInt(source.id as string)].push([parseInt(target.id as string), weight]);
      }
    }
    
    /* -----------api-----------*/

    this.http.post("http://localhost:8080/connectToApi/sendAdjList", adjList).subscribe(
      response => {
        console.log('Adjacency list sent successfully!');
      }
    )

  }

  // addEdge(source: number, target: number, weight: number){
  //   this.adjacencyList[source].push([target, weight]);
  //   console.log(this.adjacencyList);
  // }

}
