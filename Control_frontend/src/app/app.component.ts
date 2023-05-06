import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import * as joint from 'jointjs';
import * as Backbone from 'backbone';
import * as $ from 'jquery';
import * as _ from 'lodash';
import { HttpClient } from '@angular/common/http';

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
  index = 0;

  @ViewChild('popUpDisplay') myDisp!: ElementRef;
  @ViewChild('setNodesDisplay') setDisp!: ElementRef;

  forwardPaths: String[] = [];
  loops: String[] = [];
  nonTouchingLoops: String[] = [];
  mainDelta: number = 0;
  deltaForEachPath: String[] = [];
  numericTransferFunction: number = 0;

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
          // const button = document.getElementById('link');
          // if(button)
          //   button.style.backgroundColor = '#547f98';
          if(this.checkEdge(this.source, this.target)){
            if(!this.tooMuchEdges(this.source, this.target) && this.source !== this.target){
                this.connectNodesUniqueRouter(this.source, this.target, 0);
            } else { this.linking = false; alert("Maximum edges between these two nodes reached.") }
          } else {
            if(this.source == this.target){
              this.connectNodesSelfRouter(this.source, this.target, 0);
            } else {
              this.connectNodes(this.source, this.target, 0, 'manhattan');
            }
          }
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
            this.selected.attr('line/stroke', 'red');
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
            this.selected.attr('line/stroke', 'black');
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
            this.selected.attr('line/stroke', 'red');
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
      this.on0();
    })
    document.getElementById('submit-final')?.addEventListener('click', () => {
      this.off0();
      this.submit();
      this.on();
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

  connectNodes(source: joint.dia.Cell, target: joint.dia.Cell, weight: number, router: string) {
    const link = new joint.shapes.standard.Link({
      source: { id: source.id },
      target: { id: target.id },
      attrs: {
        line: {
          stroke: 'black'
        }
      },
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
    link.prop('src', source.id);
    link.prop('trg', target.id);
    link.router(router);
    link.connector('jumpover');
    this.graph.addCell(link);
    //this.linking = false;
  }

  connectNodesUniqueRouter(source: joint.dia.Cell, target: joint.dia.Cell, weight: number) {
    var router = this.getUniqueRouter();
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
    link.prop('src', source.id);
    link.prop('trg', target.id);
    link.prop('weight', weight);
    link.router(router);
    link.connector('jumpover');
    this.graph.addCell(link);
    this.linking = false;
  }

  connectNodesSelfRouter(source: joint.dia.Cell, target: joint.dia.Cell, weight: number) {
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
    link.router('orthogonal', {
      padding: 50
    });
    link.connector('jumpover');
    link.prop('src', source.id);
    link.prop('trg', target.id);
    link.prop('weight', weight);
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
    if(this.selected){
      if(this.selected instanceof joint.shapes.standard.Link){
        let source = this.selected.getSourceCell();
        let target = this.selected.getTargetCell();
        source?.attr('circle/stroke', 'black');
        target?.attr('circle/stroke', 'black');
        this.selected.attr('line/stroke', 'black');
      }else{
        this.selected.attr('circle/stroke', 'black');
      }
      this.selected = this.graph.getCell('') || null;
      this.nodeOrEdge = true;
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
      if(this.source) {
        this.source.attr('circle/stroke', 'black');
        this.source = this.graph.getCell('') || null;
      }
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
    input.value = "0";
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
    const srcInput: HTMLInputElement = document.getElementById('source') as HTMLInputElement;
    const destInput: HTMLInputElement = document.getElementById('dest') as HTMLInputElement;
    const _source = parseInt(srcInput.value);
    const _dest = parseInt(destInput.value);
    let edges = this.graph.getLinks();
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
        this.http.post("http://localhost:8080/connectToApi/sendSource", _source).subscribe(
          response => {
            console.log('Source node sent successfully!');
            this.http.post("http://localhost:8080/connectToApi/sendDestination", _dest).subscribe(
              response => {
                console.log('Destination node sent successfully!');
                this.http.get<String[]>("http://localhost:8080/connectToApi/getPathsWithGain").subscribe(
                  (list) => {
                    this.forwardPaths = list;
                    console.log(this.forwardPaths);
                    this.http.get<String[]>("http://localhost:8080/connectToApi/getLoopsWithGain").subscribe(
                      (list) => {
                        this.loops = list;
                        console.log(this.loops);
                        this.http.get<String[]>("http://localhost:8080/connectToApi/getNonTouchingLoops").subscribe(
                          (list) => {
                            this.nonTouchingLoops = list;
                            console.log(this.nonTouchingLoops);
                            this.http.get<number>("http://localhost:8080/connectToApi/getMainDelta").subscribe(
                              (delta) => {
                                this.mainDelta = delta;
                                console.log(this.mainDelta);
                                this.http.get<String[]>("http://localhost:8080/connectToApi/getDeltaForEachForwardPath").subscribe(
                                  (list) => {
                                    this.deltaForEachPath = list;
                                    console.log(this.deltaForEachPath);
                                    this.http.get<number>("http://localhost:8080/connectToApi/getTransferFunction").subscribe(
                                      (tf) => {
                                        this.numericTransferFunction = tf;
                                        console.log(this.numericTransferFunction);
                                      }
                                    )
                                  }
                                )
                              }
                            )
                          }
                        )
                      }
                    )
                  }
                )
              }
            )
          }
        )
      }
    )
  }

  checkEdge(source: joint.dia.Cell, target: joint.dia.Cell): boolean{
    let edges = this.graph.getLinks();
    for(let edge of edges){
      let _source = edge.getSourceElement();
      let _target = edge.getTargetElement();
      if(source == _source && target == _target){
        return true;
      }
    }
    return false;
  }

  getUniqueRouter() {
    var routers = ['normal', 'manhattan', 'oneSide'];
    var router = routers[this.index % routers.length];
    this.index++;
    return router;
  }

  tooMuchEdges(source: joint.dia.Cell, target: joint.dia.Cell): boolean{
    let edges = this.graph.getLinks();
    let counter = 0;
    for(let edge of edges){
      let _source = edge.getSourceElement();
      let _target = edge.getTargetElement();
      if(source == _source && target == _target){
        counter++;
        if(counter == 4){
          return true;
        }
      }
    }
    return false;
  }

  // addEdge(source: number, target: number, weight: number){
  //   this.adjacencyList[source].push([target, weight]);
  //   console.log(this.adjacencyList);
  // }

  on(){
    this.myDisp.nativeElement.style.display = 'block';
  }

  on0(){
    this.setDisp.nativeElement.style.display = 'block';
  }

  off(){
    this.myDisp.nativeElement.style.display = 'none';
  }

  off0(){
    this.setDisp.nativeElement.style.display = 'none';
  }

}
