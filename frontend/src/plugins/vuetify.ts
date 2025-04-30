/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import { createVuetify } from 'vuetify'

const lexmeaTheme = {
  dark: false,
  colors: {

    // red color definitions
    red_darkest : '#FF968B',
    red_darker : '#FFAA9F',
    red_default : '#FFB9AF',
    red_lighter : '#FFCDC1',
    red_lightest : '#FFDFD5',
    red_white : '#FFF1EC',

    //orange color definitions
    orange_darkest : '#FFA95B',
    orange_darker : '#FFB571',
    orange_default : '#FFC997',
    orange_lighter : '#FFD9B7',
    orange_lightest : '#FFEBD9',
    orange_white : '#FEF5E8',

    //yellow color definitions
    yellow_darkest : '#F9CF23',
    yellow_darker : '#FBDC5C',
    yellow_default : '#FBE481',
    yellow_lighter : '#FCECA6',
    yellow_lightest : '#FEF3C6',
    yellow_white : '#FFF3C6',

    //green color definitions
    green_darkest : '#78B390',
    green_darker : '#92C1A5',
    green_default : '#AACFB9',
    green_lighter : '#C9E1D3',
    green_lightest : '#E0EEE6',
    green_white : '#F4F9F6',

    //blue color definitions
    blue_darkest : '#5F99C5',
    blue_darker : '#86B1D6',
    blue_default : '#ABC9E2',
    blue_lighter : '#D2E0EE',
    blue_lightest : '#E6EFF6',
    blue_white : '#F3F7FB',

    //purple color definitions
    purple_darkest : '#A07498',
    purple_darker : '#B290AC',
    purple_default : '#C2A6BD',
    purple_lighter : '#D3BFD0',
    purple_lightest : '#E5DBE3',
    purple_white : '#F7F3F6',

    //burgundy color definition
    burgundy_darkest : '#674954',
    burgundy_darker : '#795663',
    burgundy_default : '#8D6574',
    burgundy_lighter : '#A7818E',
    burgundy_lightest : '#FFF3F3',

    //scheme_own color definition
    scheme_own_darkest : '#745859',
    scheme_own_darker : '#89696B',
    scheme_own_default : '#9F8082',
    scheme_own_lighter : '#B8A2A3',
    scheme_own_lightest : '#CEC0C1',

    //scheme_lexmea color definition
    scheme_lexmea_darkest : '#405B6C',
    scheme_lexmea_darker : '#4C6C80',
    scheme_lexmea_default : '#5D859D',
    scheme_lexmea_lighter : '#98B2C2',
    scheme_lexmea_lightest : '#C0D0DA',

    // selection color definitions
    selection_darkest : '#A0CCC7',
    selection_darker : '#A8D0CB', //z.B. RIng active text
    selection_default : '#B9D9D5',
    selection_lighter : '#CBE3E0',
    selection_lightest : '#DCECEA', //z.B. Spalte 1 und 4
    selection_white : '#E6F2F1',

    // pop color definitions
    pop_darkest : '#1E4A58',
    pop_darker : '#255363',
    pop_default : '#2B5D6F', // z.B: Selection library coloumn 1 , buttons, ring active text, links
    pop_lighter : '#336C80',
    pop_lightest : '#3F8391',
    pop_white : '#4794A3',

    // bluegrey color definitions
    bluegrey_900 : '#384955',
    bluegrey_800 : '#445968',
    bluegrey_700 : '#6E8898',
    bluegrey_600 : '#91A6B1',
    bluegrey_500 : '#C2CFD6',

    // lexmea_blue color definitions
    lexmea_blue_900 : '#1F2329', // z.B: Text und Überschiften WebApp
    lexmea_blue_800 : '#01121B',
    lexmea_blue_700 : '#021924',
    lexmea_blue_600 : '#032637', // z.B: Text + Icons landing
    lexmea_blue_500 : '#032E42',
    lexmea_blue_400 : '#03364D', // z.B: <h2> landing; Header; ausgewählte Reiter + Linie darunter
    lexmea_blue_300 : '#043F59',
    lexmea_blue_200 : '#054D63', // z.B: <h3> landing

    // grey color definitions
    grey_900 : '#CBD0D7',
    grey_800 : '#D1D6DD', // z.B: Dropdown Überschriften aktiv hover
    grey_700 : '#D9DEE3', // z.B: Dropdown Überschriften aktiv
    grey_600 : '#E4E8EC', // z.B: Dropdown Überschriften nicht aktiv hover
    grey_500 : '#EBEEF1', // z.B: Hintergrund Schreibtisch und Spalte 1/4 Bücherregal
    grey_400 : '#F1F3F5', // z.B: Hover Buttons
    grey_300 : '#F5F7FA',
    grey_200 : '#FBFBFF', // Boxen Schreibtisch
    grey_100 : '#FDFDFF',

    white : '#FFFFFF',
    black : '#000000',

    primary: '#03364D', // primary color == lexmea_blue_400
    secondary: '#043F59', // secondary color == lexmea_blue_300
    accent : '#336C80', // accent color == pop_lighter
    error : '#FF968B', // error color == red_darkest
    info : '#5F99C5', // info color == blue_darkest
    success : '#78B390', // success colo#r == green_darkest
    warning : '#FFA95B', // warning color == orange_darkest
    background : '#EBEEF1', // background color == grey_500
    surface : '#FFFFFF', // surface color == white
    pop : "#B9D9D5", // pop color == selection_default
  }
}

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: 'lexmeaTheme',
    themes: {
      lexmeaTheme,
    }
  },
})
