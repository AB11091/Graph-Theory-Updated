import Queue from './Queue.js';

document.addEventListener("DOMContentLoaded", function() {

    fetch('/api/removeGraph')
            .then(response => {
                if (response.text() == 1) {
                    console.log('graph removed')
                }
            })
        .catch(error => console.log(error));

    function testFunction() {
        const redDot = document.getElementById("1");
        const blueDot = document.getElementById("2");
        const line = document.getElementById("line");
        
        const startX = parseFloat(redDot.getAttribute("cx"));
        const startY = parseFloat(redDot.getAttribute("cy"));
        const endX = parseFloat(blueDot.getAttribute("cx"));
        const endY = parseFloat(blueDot.getAttribute("cy"));
        
        const animationDuration = 900; // milliseconds
        const startTime = performance.now();

        
        
        
    }
    
    const submitButton = document.getElementById("submitButton");
    const lh = 'http://localhost:8080'
    let coords = null;
    let AdjMap = null;
    const SVG_NS = "http://www.w3.org/2000/svg";
    let chmap = null;
    submitButton.addEventListener('click', function() {

        if (document.getElementById('node_group') != null) {
            return
        }
        const textField = document.getElementById("string-graph")
            .value.replace(/ /g, "m").replace(/\|/g, "l");
        
        const responseMessage = document.getElementById("responseMessage");

        
        fetch('/api/VertexCoordinates?param=' + textField)
            .then(response => {
                return response.json()
            })
            .then(data => {
                coords = data
                console.log(coords)
                console.log("complete")
                return fetch('api/AdjMap')
            })
            .then(response => {
                return response.json()
            })
            .then (data => {
                AdjMap = data
                console.log(AdjMap)
                return placeNodes()
                
            })
            .then (data => {
                return fetch('/api/CoordinateMap')
            })
            .then(cmap => {
                return cmap.json()
            })
            .then(cmap => {
                chmap = cmap
                console.log(chmap['1'])
                return placeEdges()
            })
            
        .catch(error => console.log(error));
        

    });

    document.getElementById('removeGraph').addEventListener('click', function() {
        fetch('/api/removeGraph')
            .then(response => {
                if (response.text() == 1) {
                    console.log('graph removed')
                }
            })
        .catch(error => console.log(error));

        document.getElementById('node_group').remove()
        document.getElementById('line_group').remove()
    })

    document.getElementById('BFS').addEventListener('click', function() {
        BFS()
    })

    document.getElementById('Coloring').addEventListener('click', function() {
        coloring()
    })

    function placeNodes() {

        return new Promise((resolve) => {
            let svg = document.getElementById('graphGrid')
            let i = 0
            let node_group = document.createElementNS(SVG_NS, 'g')
            node_group.setAttribute('id', 'node_group')

            const start_x = 450
            const start_y = 225
            for (const key in AdjMap) {

                const end_x = coords[i][0]
                const end_y = coords[i][1]
                const g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
                g.setAttribute('id', 'group' + key);

                let newCircle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
                newCircle.setAttribute('cx', String(start_x + 15));
                newCircle.setAttribute('cy', String(start_y + 25));
                newCircle.setAttribute('r', '15');
                newCircle.setAttribute('id', key);

                const text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                text.setAttribute('x', String(start_x + 15));
                text.setAttribute('y', String(start_y + 25 + 2));
                text.setAttribute('fill', 'white');
                text.setAttribute('text-anchor', 'middle');
                text.setAttribute('alignment-baseline', 'middle');
                text.textContent = key;

                // start = 450, end = 200
                // translate = end - start
                g.appendChild(newCircle);
                g.appendChild(text)
                g.style.setProperty('--translate', `translate(${end_x - start_x}px, ${end_y - start_y}px)`)
                g.classList.add('animate')

                node_group.appendChild(g)

                i++;
                // await sleep(200);
                
            }
            svg.appendChild(node_group)

            node_group.getBoundingClientRect();

            setTimeout(() => {
                console.log('Nodes placed');
                resolve();
            }, 2000); // Adjust the timeout to the duration of your animation
        })
        // first, scale the coordinates to the correct sizes (already done in backend)
        

        // Apply final transformations with a short delay
        // setTimeout(() => {
        //     Object.keys(AdjMap).forEach((key, i) => {
        //         const end_x = coords[i][0];
        //         const end_y = coords[i][1];
        //         const g = document.getElementById('group' + key);
        //         g.setAttribute('transform', `translate(${end_x - start_x}, ${end_y - start_y})`);
        //     });
        // }, 50); // 50ms delay to ensure reflow


    }


    function placeEdges() {

        return new Promise((resolve) => {
            let svg = document.getElementById('graphGrid')
            let line_group = document.createElementNS(SVG_NS, 'g')
            line_group.setAttribute('id', 'line_group')

            const used = new Set(); // if an edge is drawn from a--b, we dont want one from b--a
            for (const key in AdjMap) {
                const values = AdjMap[key]
                for (let i = 0; i < values.length; i++) {
                    // we will use cmap to get coordinates based on the name
                    const line = document.createElementNS("http://www.w3.org/2000/svg", "line")

                    if (!used.has(values[i] + key)) { // check value -- start
                        line.setAttribute('x1', String(chmap[key][0]  + 15));
                        line.setAttribute('y1', String(chmap[key][1] + 25));
                        line.setAttribute('x2', String(chmap[values[i]][0] + 15));
                        line.setAttribute('y2', String(chmap[values[i]][1] + 25));
                        line.setAttribute('id', key + "-" + values[i]);
                        line.setAttribute('stroke-width', '2')
                        line.setAttribute('stroke', 'white')
                        line.style.opacity = 0;
                        
                        used.add(key + values[i])
                        line_group.appendChild(line)
                    }            
                }
            }
            svg.appendChild(line_group)
            svg.insertBefore(line_group, svg.firstChild);
            line_group.getBoundingClientRect();


            

            setTimeout(() => {
                const lines = line_group.children;
                for (let line of lines) {
                    line.style.transition = 'opacity 2s';
                    line.style.opacity = 1;
                }
                // svg.appendChild(document.getElementById('node_group'))
                console.log('Edges placed');
            }, 0); // Start the animation immediately
        })
        
    }

    async function BFS() {

        console.log("test BFS")
        
        const v = "Visited Set: "
        const qe = "Queue: "
        const o = "Order: "
        const q_text = document.getElementById('queueText')
        const vs_text = document.getElementById('vsText')
        const status = document.getElementById('Operation')
        const order = document.getElementById('Order')
        const node_group = document.getElementById('node_group')
        const line_group = document.getElementById('line_group')

        order.textContent = o
        // all of these functions also edit text
        function startBlinkNode(lineOrNode) {
            lineOrNode.classList.add('blinkNode')
        }

        function stopBlinkNode(lineOrNode) {
            lineOrNode.classList.remove('blinkNode')
        }

        function startBlinkLine(line) {
            line.classList.add('blinkLine')
        }

        function stopBlinkLine(line) {
            line.classList.remove('blinkLine')
        }

        function delay() {
            return new Promise(resolve => setTimeout(resolve, 2000));
        }

        // use AdjMap
        // start with the head
        const vs = new Set();
        const q = new Queue();
        let head = null


        q.enqueue(Object.keys(AdjMap)[0])
        vs.add(Object.keys(AdjMap)[0])
        console.log(q.printQueue())
        while (!q.isEmpty()) {

            let node = q.dequeue();
            let placeholder = node_group.querySelector('#group' + node)
            let element = placeholder.querySelector('#\\3' + node)
            startBlinkNode(element)
            order.textContent += " " + node
            
            // if (vs.has(node)) {
            //     await delay();

            //     if (!q.isEmpty()) {
            //         status.textContent = node + " has already been visited. Dequeue the next one"
            //     } else {
            //         status.textContent = node + " has already been visited. BFS ends here."
            //     }
                
            //     continue
            // }

            status.textContent = "Currently visiting " + String(node) + "..."
            vs_text.textContent = v + Array.from(vs).join(' ')
            q_text.textContent = qe + q.printQueue()

            let new_explore = [];

            await delay();
            status.textContent = "Add adjacent nodes that haven't already been visited to queue"

            await delay();
            // You can continue with more code here
            for (const adj of AdjMap[node]) {
                if (!vs.has(adj)) {
                    q.enqueue(adj)
                    vs.add(adj)
                    let a_b_first = false;
                    let linea_b = line_group.querySelector('#\\3' + node + '-' + adj)
                    let lineb_a = line_group.querySelector('#\\3' + adj + '\\-' + node)
                    if (linea_b !== null) {
                        startBlinkLine(linea_b)
                        a_b_first = true
                    } else if (lineb_a !== null) {
                        startBlinkLine(lineb_a)
                    } else {
                        console.log(node + "-" + adj + " or " + adj + "-" + node + " was not found.")
                    }
                    new_explore.push(adj)
                    status.textContent = "Add " + adj + " to queue"
                    vs_text.textContent = v + Array.from(vs).join(' ')

                }
                
                q_text.textContent = qe + q.printQueue()
                await delay();
                
                
            }


            stopBlinkNode(element)
            
            if (new_explore.length == 0) {
                console.log('len 0')
                status.textContent = "Nothing to explore from here. Dequeueing the next one..."
            }

            await delay();
            for (const adj of new_explore) {
                if (line_group.querySelector('#\\3' + node + '\\-' + adj) !== null) {
                    stopBlinkLine(line_group.querySelector('#\\3' + node + '\\-' + adj))
                } else if (line_group.querySelector('#\\3' + adj + '\\-' + node) !== null) {
                    stopBlinkLine(line_group.querySelector('#\\3' + adj + '\\-' + node))
                } else {
                    console.log(node + "-" + adj + " or " + adj + "-" + node + " was not found.")
                }
            }

            console.log(q.printQueue())
            
        }


        status.textContent = "BFS is complete."
        
    }

    async function coloring() {
        let colors = []
        let q = new Queue()
        let vs = new Set()
        const status_text = document.getElementById('colorStatusText')
        const grid = document.getElementById('graphGrid')
        const rgb_values = []

        function startBlink(element) {
            element.classList.add('blinkBorder')
        }

        function stopBlink(element) {
            element.classList.remove('blinkBorder')
        }

        function delay() {
            return new Promise(resolve => setTimeout(resolve, 2000));
        }

        // INDEX
        function whichColor(node, colors) {
            for (let i = 0; i < colors.length; i++) {
                if (colors[i].has(node)) {
                    return i;
                }
            }
        }

        function assignExistingColor(curr, colors, used_colors) {
            for (let i = 0; i < colors.length; i++) {
                if (!used_colors.has(i)) {
                    return i;
                }
            }
        }
        // needed data structures
        // 1. colors: set of sets (each inner set represents all the nodes of one color)
        // 2. queue of nodes just like regular BFS (use BFS implementation)
        // 3. visited set
        // 4. hashmap {color_index : [r, g, b]} or just [[r1, g1, b1], [r2, b2, g2], ...]

        const head = Object.keys(AdjMap)[0]
        q.enqueue(head)
        vs.add(head)
        while (!(q.isEmpty() || vs.size == Object.keys(AdjMap).length)) {
            let curr = q.dequeue()

            status_text.textContent = "Visiting " + curr + "...";

            await delay(); 

            status_text.textContent = "Check the assigned colors of adjacent nodes"
            let used_colors = new Set()
            let blinking = []
            for (const adj of AdjMap[curr]) {

                await delay();
                const adj_node = document.getElementById(adj)
                startBlink(adj_node)
                blinking.push(adj_node)
                if (vs.has(adj)) {
                    const adj_color = whichColor(adj, colors)
                    used_colors.add(adj_color)
                    status_text.textContent = adj + " is already color " + adj_color + ". Node " + curr + 
                        " can't be any of these: {" + Array.from(used_colors).join(', ') + "}"          

                } else {
                    if (!(q.contains(adj))) {
                        q.enqueue(adj)
                    }
                    status_text.textContent = adj + " has no assigned color yet. Node " + curr + 
                        " can't be any of these: {" + Array.from(used_colors).join(', ') + "}"
                }
            }

            await delay()
            for (const node of blinking) {
                stopBlink(node)
            }

            await delay()

            if (used_colors.size == colors.length) {
                status_text.textContent = "We need to create a new color for Node " + curr + "."
                

                let new_color = new Set()
                new_color.add(curr)
                colors.push(new_color)
                //generate random numbers between 0 and 256 for these
                const r = Math.floor(Math.random() * 257)
                const g = Math.floor(Math.random() * 257)
                const b = Math.floor(Math.random() * 257)
                const new_rgb = [r, g, b]
                rgb_values.push(new_rgb)

                document.getElementById(curr).setAttribute('fill', `rgb(${r}, ${g}, ${b})`)
                new_color_animation(colors.length - 1, curr, r, g, b)
            } else {
                let existing_color = assignExistingColor(curr, colors, used_colors)
                status_text.textContent = "We can add Node " + curr + " to an existing color."
                    + " Through a greedy approach, we assign the first color available, which is color " + existing_color

                colors[existing_color].add(curr)
                let existing_rgb = rgb_values[existing_color]

                add_to_color_animation(document.getElementById('color' + existing_color), curr,
                    existing_rgb[0], existing_rgb[1], existing_rgb[2], colors[existing_color].size)

                    document.getElementById(curr).setAttribute('fill', `rgb(${existing_rgb[0]}, ${existing_rgb[1]}, ${existing_rgb[2]})`)
            }

            vs.add(curr)

            await delay()
        }

    }

    function new_color_animation(color_index, node_value, r, g, b) {
        const colorContainer = document.getElementById('colorContainer')

        const singleContainer = document.createElement('div')
        singleContainer.className = 'container'
        singleContainer.setAttribute('id', 'container' + color_index)

        const svg_container = document.createElement('div')
        svg_container.className = 'svg-container'
        svg_container.setAttribute('id', 'svg-container')
        
        const number = document.createElement('div')
        number.textContent = 'Color ' + color_index + ': '
        number.className = 'number'

        const svg_grid = document.createElementNS(SVG_NS, 'svg')
        svg_grid.setAttribute('id', 'svg-grid')

        const rect = document.createElementNS(SVG_NS, 'rect')
        rect.setAttribute('fill', 'darkgrey')
        rect.setAttribute('width', '500')
        rect.setAttribute('height', '50')


        const node = document.createElementNS(SVG_NS, 'g')

        const circle = document.createElementNS(SVG_NS, 'circle')
        circle.setAttribute('cx', '35')
        circle.setAttribute('cy', '25')
        const randomColor = `rgb(${r}, ${g}, ${b})`
        circle.setAttribute('fill', randomColor)
        circle.setAttribute('r', '15')

        const value = document.createElementNS(SVG_NS, 'text')
        value.setAttribute('fill', 'white')
        value.setAttribute('text-anchor', 'middle')
        value.setAttribute('alignment-baseline', 'middle')
        value.setAttribute('x', '35')
        value.setAttribute('y', '27')
        value.textContent = node_value

        node.appendChild(circle)
        node.appendChild(value)

        svg_grid.appendChild(rect)
        svg_grid.appendChild(node)

        svg_container.appendChild(svg_grid)

        singleContainer.appendChild(number)
        singleContainer.appendChild(svg_container)
        singleContainer.setAttribute('id', 'color' + color_index)

        colorContainer.appendChild(singleContainer)

    }

    function add_to_color_animation(container, node_value, r, g, b, offset) {
        const svg_grid = container.querySelector('#svg-container').querySelector('#svg-grid')

        const offset_pixels = (35 + (45 * (offset - 1))).toString()
        
        const node = document.createElementNS(SVG_NS, 'g')

        const circle = document.createElementNS(SVG_NS, 'circle')
        circle.setAttribute('cx', offset_pixels)
        circle.setAttribute('cy', '25')
        const randomColor = `rgb(${r}, ${g}, ${b})`
        circle.setAttribute('fill', randomColor)
        circle.setAttribute('r', '15')

        const value = document.createElementNS(SVG_NS, 'text')
        value.setAttribute('fill', 'white')
        value.setAttribute('text-anchor', 'middle')
        value.setAttribute('alignment-baseline', 'middle')
        value.setAttribute('x', offset_pixels)
        value.setAttribute('y', '27')
        value.textContent = node_value

        node.appendChild(circle)
        node.appendChild(value)

        svg_grid.appendChild(node)
    }
});

