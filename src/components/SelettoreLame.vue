<template>
    <div class="riga">
        <div class="lame-selettore">
            <label>Lama in uso:</label>

            <select v-model="currentLama">
                <option value="-1">Nessuna lama</option>
                <option v-for="(lama, i) in lame" :value="i">{{ lama.name }}</option>
            </select>
        </div>

        <div class="warnings">
            <button v-if="needsCambio" class="btn btn-danger">Cambio necessario</button>
            <button v-if="needsAffilatura && !needsCambio" class="btn btn-warning" @click="affila">Affilatura necessaria</button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'SelettoreLame',
    props: ['database','socket'],
    data() {
        return {
            currentLama: -1,
            needsAffilatura: false,
            needsCambio: false,
            lame: []
        }
    },
    computed: {
    },
    methods: {
        loadLame() {
            if(this.database) {
                console.error('SOCO', this.database);
                if(this.database.lame.length == 0) {
                    this.database.lama_in_uso = -1;
                    // this.socket.emit('save-db', this.database);
                }
                this.risks();
                this.currentLama = this.database.lama_in_uso;
                this.lame = this.database.lame;
            }  
        },
        risks() {
            if(this.currentLama > -1) {
                this.needsAffilatura = this.database.lame[this.currentLama].parziale > this.database.lame[this.currentLama].maxMetri;
                this.needsCambio = this.database.lame[this.currentLama].ore > this.database.lame[this.currentLama].maxOre;
            }
        },
        affila() {
            let mesi = [
                'gennaio',
                'febbario',
                'marzo',
                'aprile',
                'maggio',
                'giugno',
                'luglio',
                'agosto',
                'settembre',
                'ottobre',
                'novembre',
                'dicembre'
            ];

            let now = new Date();
            this.database.lame[this.currentLama].parziale = 0;
            this.database.lame[this.currentLama].ultimaAffilatura = now.getDate() + " " + mesi[now.getMonth()] + " " + now.getFullYear();

            // this.socket.emit('save-db', this.database);
            this.risks();
        },
        cambia() {
            
        }
    },
    mounted() {
        this.loadLame();
    },
    watch: {
        currentLama(nv) {
            this.database.lama_in_uso = nv;
            // this.socket.emit('save-db', this.database);
            this.risks();
        }
    }
}
</script>

<style scoped>
.riga {
    display: flex;
    justify-content: space-between;
    padding: 10px 20px 18px 20px;
    background-color: #fafafa;
    border-top: 1px solid #e6e6e6;
}


.lame-selettore {
    display: flex;
    flex-direction: column;
    width: 100%;
}

.lame-selettore>select {
    margin-bottom: 0;
}

.lame-selettore>label {
    height: fit-content;
    margin-bottom: 0;
    margin-bottom: 6px;
    font-size: 13px;
}

.warnings {
    display: flex;
    align-items: center;
    margin-top: 25px;
}

.warnings > button {
    white-space: nowrap;
    font-size: 14px;
}
</style>