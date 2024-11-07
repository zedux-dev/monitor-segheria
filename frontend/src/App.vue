<template>
    <selettore-lame :tabPage="tab" @change-tab="changeTab($event);" />

    <div class="content">
        <Dashboard v-if="tab == 'dashboard'" ref="dashboard" :socket="socket" :database="database" />
        <GestioneLame v-if="tab == 'lame'" />
        <ExportExcel v-if="tab == 'excel'" />
    </div>
</template>

<script>
import SelettoreLame from './components/SelettoreLame.vue';
import Dashboard from './components/Dashboard.vue';
import GestioneLame from './components/GestioneLame.vue';
import ExportExcel from './components/ExportExcel.vue';
import { io } from 'socket.io-client';

export default {
    name: 'App',
    components: {
        SelettoreLame,
        Dashboard,
        GestioneLame,
        ExportExcel
    },
    data() {
        return {
            tab: 'dashboard',
            socket: null,
            database: null,
            listeners: false,
            timeout: null,
            online: false
        }
    },
    methods: {
        heartbeat() {
            if(this.socket.connected) {
                this.socket.emit('ping');

                if(!this.listeners) {
                    this.setupListeners();
                }

                if(!this.database) {
                    this.getDatabase();
                }
            }
        },
        setupListeners() {
            if(this.socket.connected) {
                this.socket.on('pong', () => {
                    if(this.timeout) {
                        clearTimeout(this.timeout);
                        this.timeout = null;
                        this.online = true;
                    }
                });
    
                this.socket.on('get-db', (db) => {
                    this.database = db;
                });

                this.socket.on('taglio', (secondi) => {                
                    this.$refs.dashboard.newTaglio(secondi);
                })

                this.listeners = true;
            }
        },
        getDatabase() {
            if(this.socket.connected) {
                this.socket.emit('get-db');
            }
        },
        changeTab(name) {
            this.tab = name;
        }
    },
    mounted() {
        this.socket = io('http://localhost:8000');

        if(this.socket) {
            this.heartbeat();

            setInterval(() => {
                this.heartbeat();

                this.timeout = setTimeout(() => {
                    this.online = false;
                }, 2000);
            }, 1000);
        }
    }
}
</script>

<style scoped>
.content {
    width: 100%;
    display: flex;
    flex-direction: column;
    flex: 1;
    padding-top: 40px;
}
</style>