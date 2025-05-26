<script setup lang="ts">
const tab = ref('adder')

import { ref } from 'vue'
import {useDeckStore} from "@/stores/deck";

const deckStore = useDeckStore()
const color_own = "#FF968B"
const color_lexmea = "#03364D"
const color_broadcast = "#78B390"
const selected = ref<TreeNode[]>([])

const router = useRouter()
const selected_strafrecht = ref('strafrecht_lexmea')
const selected_oeffrecht = ref('oeffirecht_lexmea')
const selected_zivilrecht = ref('zivilrecht_lexmea')

const menu = ref(false)
const lexmea = ref(true)
const own = ref(false)
const broad = ref(false)

type TreeNode = {
  id: number;
  title: string;
  color?: string;
  children?: TreeNode[];
}

function menuButton() {
  if(lexmea.value && own.value && broad.value){
    selected_strafrecht.value= 'strafrecht_lexmea_broadcast_own'
    selected_oeffrecht.value= 'oeffirecht_lexmea_broadcast_own'
    selected_zivilrecht.value= 'zivilrecht_lexmea_broadcast_own'
  }
  else if (lexmea.value && own.value){
    selected_strafrecht.value= 'strafrecht_lexmea_own'
    selected_oeffrecht.value= 'oeffirecht_lexmea_own'
    selected_zivilrecht.value= 'zivilrecht_lexmea_own'
  }
  else if (lexmea.value && broad.value){
    selected_strafrecht.value= 'strafrecht_lexmea_broadcast'
    selected_oeffrecht.value= 'oeffirecht_lexmea_broadcast'
    selected_zivilrecht.value= 'zivilrecht_lexmea_broadcast'
  }
  else if (own.value && broad.value){
    selected_strafrecht.value= 'strafrecht_broadcast_own'
    selected_oeffrecht.value= 'oeffirecht_broadcast_own'
    selected_zivilrecht.value= 'zivilrecht_broadcast_own'
  }
  else if (own.value){
    selected_strafrecht.value= 'strafrecht_own'
    selected_oeffrecht.value= 'oeffirecht_own'
    selected_zivilrecht.value= 'zivilrecht_own'
  }
  else if (broad.value){
    selected_strafrecht.value= 'strafrecht_broad'
    selected_oeffrecht.value= 'oeffirecht_broad'
    selected_zivilrecht.value= 'zivilrecht_broad'
  }
  else if (lexmea.value){
    selected_strafrecht.value= 'strafrecht_lexmea'
    selected_oeffrecht.value= 'oeffirecht_lexmea'
    selected_zivilrecht.value= 'zivilrecht_lexmea'
  }
  else {
    selected_strafrecht.value= ''
    selected_oeffrecht.value= ''
    selected_zivilrecht.value= ''
  }
  selected.value = []

}


//Bäume wenn nur Lexmea als Ersteller gewählt ist:
const strafrecht_lexmea = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Lexmea)',
    color: color_lexmea

  },
  {
    id: 2,
    title: 'Strafrecht BT',
    children: [
      { id: 3, title: 'Nichtvermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 4, title: 'Vermögensdelikte (Lexmea)',color: color_lexmea},
        ],
  },
  {
        id: 5,
        title: 'Strafprozessrecht (Lexmea)',
        color: color_lexmea
  },
])

const oeffirecht_lexmea = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Lexmea)',color: color_lexmea},
      { id: 3, title: 'Staatrecht II: Grundrechte (Lexmea)',color: color_lexmea},
    ],
  },
  {
    id: 4,
    title: 'Verwaltungsrecht',
    children: [
      { id: 5, title: 'Allgemeines Verwaltungsrecht (Lexmea)',color : color_lexmea},
      { id: 6,
        title: 'Besonderes Verwaltungsrecht',
      children: [
        { id: 7, title: 'Baurecht (Lexmea)',color : color_lexmea}
      ]},
      { id: 8, title: 'Verwaltungsprozessrecht (Lexmea)',color: color_lexmea},
    ],
  },
])

const zivilrecht_lexmea = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Lexmea)',color: color_lexmea},
      {
        id: 3,
        title: 'Schuldrecht',
        children: [
          {id: 4, title: 'Schuldrecht AT (Lexmea)',color: color_lexmea},
          {
            id: 5,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 6,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 7, title: 'Kaufrecht u.Ä. (Lexmea)',color: color_lexmea}
                ]
              }
            ]
          },
        ],
      },
      { id: 8, title: 'Sachenrecht (Lexmea)',color: color_lexmea},
    ],
  },
  {
    id: 9,
    title: 'Zivilprozessrecht',
    children: [
      {id: 10, title: 'Nat. Zivilprozessrecht (Lexmea)',color: color_lexmea}
    ]
  }
])

//Bäume wenn nur eigene als Ersteller gewählt ist:
const strafrecht_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Eigner Stapel)',
    color: color_own

  },
  {
    id: 2,
    title: 'Strafrecht BT',
    children: [
      { id: 3, title: 'Nichtvermögensdelikte (Eigner Stapel)',color: color_own},
      { id: 4, title: 'Vermögensdelikte (Eigner Stapel)',color: color_own},
    ],
  },
  {
    id: 5,
    title: 'Strafprozessrecht',
    color: color_own
  },
])

const oeffirecht_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Eigner Stapel)',color: color_own},
      { id: 3, title: 'Staatrecht II: Grundrechte (Eigner Stapel)',color: color_own},
    ],
  },
  {
    id: 4,
    title: 'Verwaltungsrecht',
    children: [
      { id: 5, title: 'Allgemeines Verwaltungsrecht (Eigner Stapel)',color : color_own},
      { id: 6,
        title: 'Besonderes Verwaltungsrecht',
        children: [
          { id: 7, title: 'Baurecht (Eigner Stapel)',color : color_own}
        ]},
      { id: 8, title: 'Verwaltungsprozessrecht (Eigner Stapel)',color: color_own},
    ],
  },
])

const zivilrecht_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Eigner Stapel)',color: color_own},
      {
        id: 3,
        title: 'Schuldrecht',
        children: [
          {id: 4, title: 'Schuldrecht AT (Eigner Stapel)',color: color_own},
          {
            id: 5,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 6,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 7, title: 'Kaufrecht u.Ä. (Eigner Stapel)',color: color_own}
                ]
              }
            ]
          },
        ],
      },
      { id: 8, title: 'Sachenrecht (Eigner Stapel)',color: color_own},
    ],
  },
  {
    id: 9,
    title: 'Zivilprozessrecht',
    children: [
      {id: 10, title: 'Nat. Zivilprozessrecht (Eigner Stapel)',color: color_own}
    ]
  }
])

//Bäume wenn nur broadcast als Ersteller gewählt ist:
const strafrecht_broad = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Broadcast)',
    color: color_broadcast

  },
  {
    id: 2,
    title: 'Strafrecht BT',
    children: [
      { id: 3, title: 'Nichtvermögensdelikte (Broadcast)',color: color_broadcast},
      { id: 4, title: 'Vermögensdelikte (Broadcast)',color: color_broadcast},
    ],
  },
  {
    id: 5,
    title: 'Strafprozessrecht (Broadcast)',
    color: color_broadcast
  },
])

const oeffirecht_broad = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Broadcast)',color: color_broadcast},
      { id: 3, title: 'Staatrecht II: Grundrechte (Broadcast)',color: color_broadcast},
    ],
  },
  {
    id: 4,
    title: 'Verwaltungsrecht',
    children: [
      { id: 5, title: 'Allgemeines Verwaltungsrecht (Broadcast)',color : color_broadcast},
      { id: 6,
        title: 'Besonderes Verwaltungsrecht',
        children: [
          { id: 7, title: 'Baurecht (Broadcast)',color : color_broadcast}
        ]},
      { id: 8, title: 'Verwaltungsprozessrecht (Broadcast)',color: color_broadcast},
    ],
  },
])

const zivilrecht_broad = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Broadcast)',color: color_broadcast},
      {
        id: 3,
        title: 'Schuldrecht',
        children: [
          {id: 4, title: 'Schuldrecht AT (Broadcast)',color: color_broadcast},
          {
            id: 5,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 6,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 7, title: 'Kaufrecht u.Ä. (Broadcast)',color: color_broadcast}
                ]
              }
            ]
          },
        ],
      },
      { id: 8, title: 'Sachenrecht (Broadcast)',color: color_broadcast},
    ],
  },
  {
    id: 9,
    title: 'Zivilprozessrecht',
    children: [
      {id: 10, title: 'Nat. Zivilprozessrecht (Broadcast)',color: color_broadcast}
    ]
  }
])

//Bäume wenn nur eigene und lexmea als Ersteller gewählt ist:
const strafrecht_lexmea_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Lexmea)',
    color: color_lexmea

  },
  {
    id: 2,
    title: 'Strafrecht AT (Eigener Stapel)',
    color: color_own

  },
  {
    id: 3,
    title: 'Strafrecht BT',
    children: [
      { id: 4, title: 'Nichtvermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 5, title: 'Vermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 6, title: 'Nichtvermögensdelikte (Eigener Stapel)',color: color_own},
      { id: 7, title: 'Vermögensdelikte (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 8,
    title: 'Strafprozessrecht (Lexmea)',
    color: color_lexmea
  },
  {
    id: 9,
    title: 'Strafprozessrecht (Eigener Stapel)',
    color: color_own
  },
])

const oeffirecht_lexmea_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Lexmea)',color: color_lexmea},
      { id: 3, title: 'Staatrecht II: Grundrechte (Lexmea)',color: color_lexmea},
      { id: 4, title: 'Staatrecht I: Staatsorganisationsrecht (Eigener Stapel)',color: color_own},
      { id: 5, title: 'Staatrecht II: Grundrechte (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 6,
    title: 'Verwaltungsrecht',
    children: [
      { id: 7, title: 'Allgemeines Verwaltungsrecht (Lexmea)',color : color_lexmea},
      { id: 8, title: 'Allgemeines Verwaltungsrecht (Eigener Stapel)',color : color_own},
      { id: 9,
        title: 'Besonderes Verwaltungsrecht',
        children: [
          { id: 10, title: 'Baurecht (Lexmea)',color : color_lexmea},
          { id: 11, title: 'Baurecht (Eigener Stapel)',color : color_own}
        ]},
      { id: 12, title: 'Verwaltungsprozessrecht (Lexmea)',color: color_lexmea},
      { id: 13, title: 'Verwaltungsprozessrecht (Eigener Stapel)',color: color_own},
    ],
  },
])

const zivilrecht_lexmea_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Lexmea)',color: color_lexmea},
      {id: 3, title: 'BGB AT (Eigener Stapel)',color: color_own},
      {
        id: 4,
        title: 'Schuldrecht',
        children: [
          {id: 5, title: 'Schuldrecht AT (Lexmea)',color: color_lexmea},
          {id: 6, title: 'Schuldrecht AT (Eigener Stapel)',color: color_own},
          {
            id: 7,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 8,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 9, title: 'Kaufrecht u.Ä. (Lexmea)',color: color_lexmea},
                  {id: 10, title: 'Kaufrecht u.Ä. (Eigener Stapel)',color: color_own}
                ]
              }
            ]
          },
        ],
      },
      { id: 11, title: 'Sachenrecht (Lexmea)',color: color_lexmea},
      { id: 12, title: 'Sachenrecht (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 13,
    title: 'Zivilprozessrecht',
    children: [
      {id: 14, title: 'Nat. Zivilprozessrecht (Lexmea)',color: color_lexmea},
      {id: 15, title: 'Nat. Zivilprozessrecht (Eigener Stapel)',color: color_own}
    ]
  }
])


//Bäume wenn nur eigene und Broadcast als Ersteller gewählt ist:
const strafrecht_broadcast_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Broadcast)',
    color: color_broadcast

  },
  {
    id: 2,
    title: 'Strafrecht AT (Eigener Stapel)',
    color: color_own

  },
  {
    id: 3,
    title: 'Strafrecht BT',
    children: [
      { id: 4, title: 'Nichtvermögensdelikte (Broadcast)',color: color_broadcast},
      { id: 5, title: 'Vermögensdelikte (Broadcast)',color: color_broadcast},
      { id: 6, title: 'Nichtvermögensdelikte (Eigener Stapel)',color: color_own},
      { id: 7, title: 'Vermögensdelikte (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 8,
    title: 'Strafprozessrecht (Broadcast)',
    color: color_broadcast
  },
  {
    id: 9,
    title: 'Strafprozessrecht (Eigener Stapel)',
    color: color_own
  },
])

const oeffirecht_broadcast_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Broadcast)',color: color_broadcast},
      { id: 3, title: 'Staatrecht II: Grundrechte (Broadcast)',color: color_broadcast},
      { id: 4, title: 'Staatrecht I: Staatsorganisationsrecht (Eigener Stapel)',color: color_own},
      { id: 5, title: 'Staatrecht II: Grundrechte (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 6,
    title: 'Verwaltungsrecht',
    children: [
      { id: 7, title: 'Allgemeines Verwaltungsrecht (Broadcast)',color : color_broadcast},
      { id: 8, title: 'Allgemeines Verwaltungsrecht (Eigener Stapel)',color : color_own},
      { id: 9,
        title: 'Besonderes Verwaltungsrecht',
        children: [
          { id: 10, title: 'Baurecht (Broadcast)',color : color_broadcast},
          { id: 11, title: 'Baurecht (Eigener Stapel)',color : color_own}
        ]},
      { id: 12, title: 'Verwaltungsprozessrecht (Broadcast)',color: color_broadcast},
      { id: 13, title: 'Verwaltungsprozessrecht (Eigener Stapel)',color: color_own},
    ],
  },
])

const zivilrecht_broadcast_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Broadcast)',color: color_broadcast},
      {id: 3, title: 'BGB AT (Eigener Stapel)',color: color_own},
      {
        id: 4,
        title: 'Schuldrecht',
        children: [
          {id: 5, title: 'Schuldrecht AT (Broadcast)',color: color_broadcast},
          {id: 6, title: 'Schuldrecht AT (Eigener Stapel)',color: color_own},
          {
            id: 7,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 8,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 9, title: 'Kaufrecht u.Ä. (Broadcast)',color: color_broadcast},
                  {id: 10, title: 'Kaufrecht u.Ä. (Eigener Stapel)',color: color_own}
                ]
              }
            ]
          },
        ],
      },
      { id: 11, title: 'Sachenrecht (Broadcast)',color: color_broadcast},
      { id: 12, title: 'Sachenrecht (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 13,
    title: 'Zivilprozessrecht',
    children: [
      {id: 14, title: 'Nat. Zivilprozessrecht (Broadcast)',color: color_broadcast},
      {id: 15, title: 'Nat. Zivilprozessrecht (Eigener Stapel)',color: color_own}
    ]
  }
])

//Bäume wenn nur Broadcast und lexmea als Ersteller gewählt ist:
const strafrecht_lexmea_broadcast = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Lexmea)',
    color: color_lexmea

  },
  {
    id: 2,
    title: 'Strafrecht AT (Broadcast)',
    color: color_broadcast

  },
  {
    id: 3,
    title: 'Strafrecht BT',
    children: [
      { id: 4, title: 'Nichtvermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 5, title: 'Vermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 6, title: 'Nichtvermögensdelikte (Broadcast)',color: color_broadcast},
      { id: 7, title: 'Vermögensdelikte (Broadcast)',color: color_broadcast},
    ],
  },
  {
    id: 8,
    title: 'Strafprozessrecht (Lexmea)',
    color: color_lexmea
  },
  {
    id: 9,
    title: 'Strafprozessrecht (Broadcast)',
    color: color_broadcast
  },
])

const oeffirecht_lexmea_broadcast = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Lexmea)',color: color_lexmea},
      { id: 3, title: 'Staatrecht II: Grundrechte (Lexmea)',color: color_lexmea},
      { id: 4, title: 'Staatrecht I: Staatsorganisationsrecht (Broadcast)',color: color_broadcast},
      { id: 5, title: 'Staatrecht II: Grundrechte (Broadcast)',color: color_broadcast},
    ],
  },
  {
    id: 6,
    title: 'Verwaltungsrecht',
    children: [
      { id: 7, title: 'Allgemeines Verwaltungsrecht (Lexmea)',color : color_lexmea},
      { id: 8, title: 'Allgemeines Verwaltungsrecht (Broadcast)',color : color_broadcast},
      { id: 9,
        title: 'Besonderes Verwaltungsrecht',
        children: [
          { id: 10, title: 'Baurecht (Lexmea)',color : color_lexmea},
          { id: 11, title: 'Baurecht (Broadcast)',color : color_broadcast}
        ]},
      { id: 12, title: 'Verwaltungsprozessrecht (Lexmea)',color: color_lexmea},
      { id: 13, title: 'Verwaltungsprozessrecht (Broadcast)',color: color_broadcast},
    ],
  },
])

const zivilrecht_lexmea_broadcast = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Lexmea)',color: color_lexmea},
      {id: 3, title: 'BGB AT (Broadcast)',color: color_broadcast},
      {
        id: 4,
        title: 'Schuldrecht',
        children: [
          {id: 5, title: 'Schuldrecht AT (Lexmea)',color: color_lexmea},
          {id: 6, title: 'Schuldrecht AT (Broadcast)',color: color_broadcast},
          {
            id: 7,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 8,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 9, title: 'Kaufrecht u.Ä. (Lexmea)',color: color_lexmea},
                  {id: 10, title: 'Kaufrecht u.Ä. (Broadcast)',color: color_broadcast}
                ]
              }
            ]
          },
        ],
      },
      { id: 11, title: 'Sachenrecht (Lexmea)',color: color_lexmea},
      { id: 12, title: 'Sachenrecht (Broadcast)',color: color_broadcast},
    ],
  },
  {
    id: 13,
    title: 'Zivilprozessrecht',
    children: [
      {id: 14, title: 'Nat. Zivilprozessrecht (Lexmea)',color: color_lexmea},
      {id: 15, title: 'Nat. Zivilprozessrecht (Broadcast)',color: color_broadcast}
    ]
  }
])

//Bäume wenn Eigene,  Broadcast und lexmea als Ersteller gewählt ist:
const strafrecht_lexmea_broadcast_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Strafrecht AT (Lexmea)',
    color: color_lexmea

  },
  {
    id: 2,
    title: 'Strafrecht AT (Broadcast)',
    color: color_broadcast

  },
  {
    id: 3,
    title: 'Strafrecht AT (Eigener Stapel)',
    color: color_own

  },
  {
    id: 4,
    title: 'Strafrecht BT',
    children: [
      { id: 5, title: 'Nichtvermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 6, title: 'Vermögensdelikte (Lexmea)',color: color_lexmea},
      { id: 7, title: 'Nichtvermögensdelikte (Broadcast)',color: color_broadcast},
      { id: 8, title: 'Vermögensdelikte (Broadcast)',color: color_broadcast},
      { id: 9, title: 'Nichtvermögensdelikte (Eigener Stapel)',color: color_own},
      { id: 10, title: 'Vermögensdelikte (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 11,
    title: 'Strafprozessrecht (Lexmea)',
    color: color_lexmea
  },
  {
    id: 12,
    title: 'Strafprozessrecht (Broadcast)',
    color: color_broadcast
  },
  {
    id: 13,
    title: 'Strafprozessrecht (Eigener Stapel)',
    color: color_own
  },
])

const oeffirecht_lexmea_broadcast_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Verfassungsrecht',
    children: [
      { id: 2, title: 'Staatrecht I: Staatsorganisationsrecht (Lexmea)',color: color_lexmea},
      { id: 3, title: 'Staatrecht II: Grundrechte (Lexmea)',color: color_lexmea},
      { id: 4, title: 'Staatrecht I: Staatsorganisationsrecht (Broadcast)',color: color_broadcast},
      { id: 5, title: 'Staatrecht II: Grundrechte (Broadcast)',color: color_broadcast},
      { id: 6, title: 'Staatrecht I: Staatsorganisationsrecht (Eigener Stapel)',color: color_own},
      { id: 7, title: 'Staatrecht II: Grundrechte (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 8,
    title: 'Verwaltungsrecht',
    children: [
      { id: 9, title: 'Allgemeines Verwaltungsrecht (Lexmea)',color : color_lexmea},
      { id: 10, title: 'Allgemeines Verwaltungsrecht (Broadcast)',color : color_broadcast},
      { id: 11, title: 'Allgemeines Verwaltungsrecht (Eigener Stapel)',color : color_own},
      { id: 12,
        title: 'Besonderes Verwaltungsrecht',
        children: [
          { id: 13, title: 'Baurecht (Lexmea)',color : color_lexmea},
          { id: 14, title: 'Baurecht (Broadcast)',color : color_broadcast},
          { id: 15, title: 'Baurecht (Eigener Stapel)',color : color_own}
        ]},
      { id: 16, title: 'Verwaltungsprozessrecht (Lexmea)',color: color_lexmea},
      { id: 17, title: 'Verwaltungsprozessrecht (Broadcast)',color: color_broadcast},
      { id: 18, title: 'Verwaltungsprozessrecht (Eigener Stapel)',color: color_own},
    ],
  },
])

const zivilrecht_lexmea_broadcast_own = ref<TreeNode[]>([
  {
    id: 1,
    title: 'Bürgerliches Recht',
    children: [
      {id: 2, title: 'BGB AT (Lexmea)',color: color_lexmea},
      {id: 3, title: 'BGB AT (Broadcast)',color: color_broadcast},
      {id: 4, title: 'BGB AT (Eigener Stapel)',color: color_own},
      {
        id: 5,
        title: 'Schuldrecht',
        children: [
          {id: 6, title: 'Schuldrecht AT (Lexmea)',color: color_lexmea},
          {id: 7, title: 'Schuldrecht AT (Broadcast)',color: color_broadcast},
          {id: 8, title: 'Schuldrecht AT (Eigener Stapel)',color: color_own},
          {
            id: 9,
            title: 'Schuldrecht BT',
            children: [
              {
                id: 10,
                title: 'Vertragliche Schuldverhältnisse',
                children: [
                  {id: 11, title: 'Kaufrecht u.Ä. (Lexmea)',color: color_lexmea},
                  {id: 12, title: 'Kaufrecht u.Ä. (Broadcast)',color: color_broadcast},
                  {id: 13, title: 'Kaufrecht u.Ä. (Eigener Stapel)',color: color_own}
                ]
              }
            ]
          },
        ],
      },
      { id: 14, title: 'Sachenrecht (Lexmea)',color: color_lexmea},
      { id: 15, title: 'Sachenrecht (Broadcast)',color: color_broadcast},
      { id: 16, title: 'Sachenrecht (Eigener Stapel)',color: color_own},
    ],
  },
  {
    id: 17,
    title: 'Zivilprozessrecht',
    children: [
      {id: 18, title: 'Nat. Zivilprozessrecht (Lexmea)',color: color_lexmea},
      {id: 19, title: 'Nat. Zivilprozessrecht (Broadcast)',color: color_broadcast},
      {id: 20, title: 'Nat. Zivilprozessrecht (Eigener Stapel)',color: color_own}
    ]
  }
])

function addCards() {
    for(const selecteddeck of selected.value ){
      deckStore.addDeck(selecteddeck.title,selecteddeck.color)
    }
    router.go(-1)
}

const trees = [
  {name: 'strafrecht_lexmea_broadcast_own', data: strafrecht_lexmea_broadcast_own},
  {name: 'oeffirecht_lexmea_broadcast_own', data: oeffirecht_lexmea_broadcast_own},
  {name: 'zivilrecht_lexmea_broadcast_own', data: zivilrecht_lexmea_broadcast_own},
  {name: 'strafrecht_lexmea_own', data: strafrecht_lexmea_own},
  {name: 'oeffirecht_lexmea_own', data: oeffirecht_lexmea_own},
  {name: 'zivilrecht_lexmea_own', data: zivilrecht_lexmea_own},
  {name: 'strafrecht_lexmea_broadcast', data: strafrecht_lexmea_broadcast},
  {name: 'oeffirecht_lexmea_broadcast', data: oeffirecht_lexmea_broadcast},
  {name: 'zivilrecht_lexmea_broadcast', data: zivilrecht_lexmea_broadcast},
  {name: 'strafrecht_broadcast_own', data: strafrecht_broadcast_own},
  {name: 'oeffirecht_broadcast_own', data: oeffirecht_broadcast_own},
  {name: 'zivilrecht_broadcast_own', data: zivilrecht_broadcast_own},
  {name: 'strafrecht_own', data: strafrecht_own},
  {name: 'oeffirecht_own', data: oeffirecht_own},
  {name: 'zivilrecht_own', data: zivilrecht_own},
  {name: 'strafrecht_broad', data: strafrecht_broad},
  {name: 'oeffirecht_broad', data: oeffirecht_broad},
  {name: 'zivilrecht_broad', data: zivilrecht_broad},
  {name: 'strafrecht_lexmea', data: strafrecht_lexmea},
  {name: 'oeffirecht_lexmea', data: oeffirecht_lexmea},
  {name: 'zivilrecht_lexmea', data: zivilrecht_lexmea},
]

const strafrechtTree = computed(() => {
  for (const tree of trees) {
    if(tree.name === selected_strafrecht.value)
    {
      return tree.data.value
    }
  }
  return []

})

const oeffrechtTree = computed(() => {
  for (const tree of trees) {
    if(tree.name === selected_oeffrecht.value)
    {
      return tree.data.value
    }
  }
  return []

})

const zivilrechtTree = computed(() => {
  for (const tree of trees) {
    if(tree.name === selected_zivilrecht.value)
    {
      return tree.data.value
    }
  }
  return []

})


</script>

<template>
  <v-responsive>
    <v-card>
      <v-tabs
        v-model="tab"
        bg-color="primary"
      >
        <v-tab value="Strafrecht">
          Strafrecht
        </v-tab>
        <v-tab value="Öffentliches Recht">
          Öffentliches Recht
        </v-tab>
        <v-tab value="Zivilrecht">
          Zivilrecht
        </v-tab>
      </v-tabs>
      <v-card-text>
        <v-tabs-window v-model="tab">
          <v-tabs-window-item value="Strafrecht">
            <v-treeview
              v-model:selected="selected"
              :items="strafrechtTree"
              item-value="id"
              selectable
              select-strategy="leaf"
              return-object
            >
              <template #title="{item}">
                <span :style="{color: item.color || 'inherit'}">
                  {{ item.title }}
                </span>
              </template>
            </v-treeview>
          </v-tabs-window-item>
          <v-tabs-window-item value="Öffentliches Recht">
            <v-treeview
              v-model:selected="selected"
              :items="oeffrechtTree"
              item-value="id"
              selectable
              select-strategy="leaf"
              return-object
            >
              <template #title="{item}">
                <span :style="{color: item.color || 'inherit'}">
                  {{ item.title }}
                </span>
              </template>
            </v-treeview>
          </v-tabs-window-item>
          <v-tabs-window-item value="Zivilrecht">
            <v-treeview
              v-model:selected="selected"
              :items="zivilrechtTree"
              item-value="id"
              selectable
              select-strategy="leaf"
              return-object
            >
              <template #title="{item}">
                <span :style="{color: item.color || 'inherit'}">
                  {{ item.title }}
                </span>
              </template>
            </v-treeview>
          </v-tabs-window-item>
        </v-tabs-window>
      </v-card-text>
      <v-card-actions>
        <v-row dense>
          <v-btn
            variant="outlined"
            @click="addCards"
          >
            Karteikarten aktivieren
          </v-btn>
          <v-spacer />

          <v-menu
            v-model="menu"
            :close-on-content-click="false"
            location="bottom"
          >
            <template #activator="{ props }">
              <v-btn
                v-bind="props"
                icon="mdi-filter"
                color="primary"
              />
            </template>
            <v-card min-width="300">
              <v-list>
                <v-list-item>
                  <v-switch
                    v-model="lexmea"
                    color="primary"
                    label="Lexmea"
                    hide-details
                  />
                </v-list-item>
                <v-list-item>
                  <v-switch
                    v-model="broad"
                    color="primary"
                    label="Broadcast"
                    hide-details
                  />
                </v-list-item>
                <v-list-item>
                  <v-switch
                    v-model="own"
                    color="primary"
                    label="Eigene Stapel"
                    hide-details
                  />
                </v-list-item>
              </v-list>
              <v-card-actions>
                <v-spacer />
                <v-btn
                  variant="text"
                  color="primary"
                  @click="menu = false;menuButton()"
                >
                  speichern
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-menu>
        </v-row>
      </v-card-actions>
    </v-card>
  </v-responsive>
</template>

<style scoped lang="sass">

</style>
