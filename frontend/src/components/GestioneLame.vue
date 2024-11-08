<template>
    <div class="colonna main">
        <div class="riga">
            <div class="lista-lame">
                <div v-for="(lama, i) in lame" :class="inEdit == i ? 'active' : ''" @click="editLamaSet(i)">{{ lama.name }}</div>
            </div>

            <div class="colonna">
                <div v-if="inEdit > -1 && tmp" class="editor">
                    <h3>Modifica lama</h3>

                    <div class="field-wrapper">
                        <span><small>Metri:</small> {{ parseFloat(tmp.metri).toFixed(2) }} m</span>
                        <span><small>Ore:</small> {{ parseFloat(tmp.ore).toFixed(2) }} m</span>
                        <span v-if="tmp.ultimaAffilatura"><small>Affilata:</small> {{ tmp.ultimaAffilatura }}</span>
                    </div>

                    <div class="field-wrapper">
                        <label>Nome</label>
                        <div class="input">
                            <input type="text" v-model="tmp.name">
                        </div>
                    </div>

                    <div class="field-wrapper">
                        <label>Metri massimi</label>
                        <div class="input">
                            <input type="text" v-model="tmp.maxMetri" @change="tmp.maxMetri = filterComma(tmp.maxMetri)">
                            <small>m</small>
                        </div>
                    </div>

                    <div class="field-wrapper">
                        <label>Ore massime</label>
                        <div class="input">
                            <input type="text" v-model="tmp.maxOre" @change="tmp.maxOre = filterComma(tmp.maxOre)">
                            <small>h</small>
                        </div>
                    </div>

                    <div style="display: flex; align-items: center; justify-content: space-between; margin-top: 14px;">
                        <button class="btn btn-primary" style="flex: 1; margin-right: 5px;" @click="save">Salva</button>
                        <button class="btn btn-danger" @click="remove">Elimina</button>
                    </div>
                </div>

                <div v-if="inEdit == -1 && tmp" class="editor">
                    <h3>Nuova lama</h3>

                    <div class="field-wrapper">
                        <label>Nome</label>
                        <div class="input">
                            <input type="text" v-model="tmp.name">
                        </div>
                    </div>

                    <div class="field-wrapper">
                        <label>Metri massimi</label>
                        <div class="input">
                            <input type="text" v-model="tmp.maxMetri" @change="tmp.maxMetri = filterComma(tmp.maxMetri)">
                            <small>m</small>
                        </div>
                    </div>

                    <div class="field-wrapper">
                        <label>Ore massime</label>
                        <div class="input">
                            <input type="text" v-model="tmp.maxOre"  @change="tmp.maxOre = filterComma(tmp.maxOre)">
                            <small>h</small>
                        </div>
                    </div>

                    <div style="display: flex; align-items: center; justify-content: space-between; margin-top: 14px;">
                        <button class="btn btn-primary" style="flex: 1; margin-right: 5px;" @click="save">Salva</button>
                        <button class="btn" @click="inEdit = -1; tmp = null;">Annulla</button>
                    </div>
                </div>
            </div>
        </div>

        <button class="btn btn-primary" style="width: fit-content; margin-top: 8px;" @click="newLamaSet">Nuova lama</button>
    </div>
</template>

<script>
export default {
    name: 'GestioneLame',
    props: ['database','socket'],
    data() {
        return {
            lame: [],
            inEdit: -1,
            tmp: null
        }
    },
    computed: {
    },
    methods: {
        loadLame() {
            if(this.database) {
                this.lame = this.database.lame;
                this.inEdit = -1;
            } else {
                setTimeout(() => {
                    this.lame = this.database.lame;
                }, 1000);
            }
        },
        newLamaSet() {
            this.inEdit = -1;
            this.tmp = {
                name: '',
                maxOre: 1,
                maxMetri: 1,
                ore: 0,
                metri: 0,
                parziale: 0,
                ultimaAffilatura: null
            }
        },
        editLamaSet(i) {
            console.error('SOCO', this.inEdit);
            this.inEdit = i;
            this.tmp = this.database.lame[i];
            console.error('SOCO', this.tmp);
        },
        filterNumbers(string) {
            if (string == "") string = "0";
            let firstCommaIndex = string.indexOf('.');
            let result = string.replace(/\D/g, '');
            result = result.replace(/\.(?=(.*\.)*)/g, '');
            if (firstCommaIndex !== -1) {
                result = result.slice(0, firstCommaIndex) + '.' + result.slice(firstCommaIndex);
            }
            return result == "" ? 0 : result
        },
        filterComma(string) {
            if (string.endsWith('.')) {
                return string.replace('.', '');
            }
            return string;
        },
        save() {
            if(this.inEdit > -1) {
                this.database.lame[this.inEdit] = this.tmp;
                console.error('SOCO', this.inEdit);
                console.error('SOCO', this.database.lame[this.inEdit]);
                this.tmp = null;
                this.inEdit = -1;
            } else {
                this.database.lame.push(this.tmp);
                this.tmp = null;
                this.inEdit = -1;
            }
            this.socket.emit('save-db', this.database);
        },
        remove() {
            this.database.splice(this.inEdit, 1);
            this.inEdit = -1;
            this.tmp = null;
            this.socket.emit('save-db', this.database);
        }
    },
    mounted() {
        this.loadLame();
    },
    watch: {
        'tmp.maxMetri'(nv) {
            if(nv) this.tmp.maxMetri = this.filterNumbers(nv.toString());
        },
        'tmp.maxOre'(nv) {
            if(nv) this.tmp.maxOre = this.filterNumbers(nv.toString());
        }
    }
}
</script>

<style scoped>
.lista-lame {
    display: flex;
    flex-direction: column;
    border-radius: 4px;
    box-shadow: 0 1px 4px rgba(0,0,0,.065);
    border: 1px solid rgb(223, 223, 223);
    width: 200px;
    overflow: hidden;
}

.lista-lame > div {
    width: 100%;
    height: 38px;
    display: flex;
    align-items: center;
    padding: 0 12px;
    cursor: pointer;
}

.lista-lame > div.active {
    background-color: #0088cc;
    color: white;
}

.lista-lame > div.active:hover {
    background-color: #0088cc;
}

.lista-lame > div:hover {
    background-color: rgb(238, 238, 238);
}

.riga {
    width: 100%;
    display: flex;
}

.colonna {
    display: flex;
    flex-direction: column;
}

.main.colonna {
    padding: 10px;
    justify-content: space-between;
    flex: 1;
}

.editor {
    display: flex;
    flex-direction: column;
    width: 200px;
    box-shadow: 0 1px 4px rgba(0,0,0,.065);
    border: 1px solid rgb(223, 223, 223);
    border-radius: 4px;
    padding: 12px 14px;
    margin-left: 50px;
}

.editor > h3 {
    font-size: 16px;
    color: #0088cc;
    font-weight: 500;
    margin-top: 0;
    line-height: 14px;
    margin-bottom: 10px;
}

.field-wrapper {
    position: relative;
    display: flex;
    flex-direction: column;
    margin-bottom: 8px;
}

.field-wrapper>label {
    font-size: 13px;
}

.field-wrapper>.input {
    width: 100%;
    height: 34px;
    display: flex;
    align-items: center;
    border: 1px solid #ccc;
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    border-radius: 4px;
}

.field-wrapper>.input:has(input:focus) {
    border-color: rgba(82, 168, 236, 0.8);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(82, 168, 236, 0.6);
}

.field-wrapper>.input>input {
    background: none;
    border: 0;
    width: 100%;
    text-align: right;
    height: 34px;
    margin-top: 9px;
    box-shadow: none;
    font-size: 14px;
    color: #555;
}

.field-wrapper>.input>small {
    opacity: 0.5;
    margin-right: 8px;
}

input.readonly {
    background-color: white;
    cursor: default;
    pointer-events: none;
}

.field-wrapper > .input:has(button) {
    border: 0;
    box-shadow: none;
}

.field-wrapper > span {
    font-size: 13px;
}

.field-wrapper > span > small {
    display: inline-block;
    width: 60px;
    opacity: 0.6;
}
</style>