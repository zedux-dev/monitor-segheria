import express from 'express';
import { Server } from 'socket.io';
import { createServer } from 'node:http';
import bodyParser from 'body-parser';
import cors from 'cors';
import fs from 'fs';

const app = express();
const server = createServer(app);
const io = new Server(server, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"]
    }
});

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

let socket = null;

io.on('connection', (sock) => {
    socket = sock;

    sock.on('ping', () => {
        sock.emit('pong');
    });

    sock.on('get-db', () => {
        let base = {
            lame: [],
            tagli: [],
            lama_in_uso: null
        };

        if(!fs.existsSync('db.json')) {
            fs.writeFileSync('db.json', JSON.stringify(base, null, 4));
        }

        let db = fs.readFileSync('db.json', 'utf8');
        db = JSON.parse(db);

        console.error('SOCO cacca', db);

        sock.emit('get-db', db);
    });

    sock.on('save-db', (db) => {
        if(db) {
            fs.writeFileSync('db.json', JSON.stringify(db, null, 4));
        }
    })
});

app.post('/taglio', (req, res) => {
    if(socket) {
        socket.emit("taglio", req.body.value)
    }

    res.sendStatus(200);
});

server.listen(8000, () => {
    console.log('Http server running at: http://192.168.1.100:8000');
});