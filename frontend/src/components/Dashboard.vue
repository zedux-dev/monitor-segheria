<template>
    <div class="dashboard">
        <div class="row">
            <div class="field-wrapper small-input">
                <label>Tagli</label>
                <input type="text" :value="tagliTotali" class="input-block-level readonly" readonly>
            </div>

            <div class="field-wrapper">
                <label>Metri tagliati</label>
                <input type="text" :value="metriTotali" class="input-block-level readonly" readonly>
                <small>m</small>
            </div>
    
            <div class="field-wrapper">
                <label>Tempo di taglio</label>
                <input type="text" :value="timeClock" class="input-block-level readonly" readonly>
            </div>

            <div class="field-wrapper">
                <label>Volume</label>
                <input type="text" :value="volume" class="input-block-level readonly" readonly>
                <small>m<sup>2</sup></small>
            </div>
        </div>

        s {{ volume }}

        <div class="row">
            <div class="field-wrapper">
                <label>Nome cliente</label>
                <input type="text" v-model="volume" class="input-block-level">
            </div>

            <div class="field-wrapper">
                <label>lunghezza</label>
                <input type="text" class="input-block-level">
                <small>cm</small>
            </div>

            <div class="field-wrapper">
                <label>Larghezza</label>
                <input type="text" class="input-block-level">
                <small>cm</small>
            </div>

            <div class="field-wrapper">
                <label>Spessore</label>
                <input type="text" class="input-block-level">
                <small>cm</small>
            </div>

            <div class="field-wrapper">
                <label>Diametro</label>
                <input type="text" class="input-block-level">
                <small>cm</small>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Dashboard',
    props: {
        socket: {
            type: Object,
            required: true
        },
        database: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            volume: 0,
            tagliTotali: 0,
            metriTotali: 0,
            secondiTotali: 3532,
            currentTaglio: {
                cliente: "",
                lunghezza: 200,
                larghezza: 100,
                spessore: 200,
                diametro: 0,
                durata: 0
            }
        }
    },
    computed: {
        timeClock() {
            let sec_num = parseInt(this.secondiTotali, 10);
            let hours   = Math.floor(sec_num / 3600);
            let minutes = Math.floor((sec_num - (hours * 3600)) / 60);
            let seconds = sec_num - (hours * 3600) - (minutes * 60);

            if (hours   < 10) {hours   = "0"+hours;}
            if (minutes < 10) {minutes = "0"+minutes;}
            if (seconds < 10) {seconds = "0"+seconds;}

            return hours + ':' + minutes + ':' + seconds;
        }
    },
    methods: {
        newTaglio(secondi) {
            this.currentTaglio.durata = secondi;
            this.secondiTotali += secondi;
            this.metriTotali += this.currentTaglio.lunghezza;
            this.volume += ((this.currentTaglio.lunghezza / 100) * (this.currentTaglio.larghezza / 100) * (this.currentTaglio.spessore / 100));
            this.tagliTotali++;

            this.database.tagli.push(this.currentTaglio);
            this.socket.emit('save-db', this.database);

            this.currentTaglio.durata = 0;
        }
    },
    mounted() {
    }
}
</script>

<style scoped>
.dashboard {
    display: flex;
    flex-direction: column;
    padding: 10px 30px;
}

.field-wrapper.small-input {
    max-width: 100px;
}

.field-wrapper {
    position: relative;
    display: flex;
    flex-direction: column;
    max-width: 150px;
    padding: 10px;
}

.field-wrapper > input {
    text-align: right;
}

.field-wrapper > label {
    cursor: default;
    font-size: 13px;
}

.field-wrapper:has(small) input {
    padding-right: 25px;
}

.field-wrapper > small {
    position: absolute;
    bottom: 24px;
    right: 20px;
    opacity: 0.5;
    cursor: default;
}

.field-wrapper:has(sup) > input {
    padding-right: 30px;
}

input.readonly {
    background-color: white;
    cursor: default;
    pointer-events: none;
}

.row {
    display: flex;
    border-bottom: 1px solid #ebebeb;
}

.row:last-child {
    border-bottom: 0;
}
</style>