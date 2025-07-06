export default interface Card {
  id : number,
  deckID:number,
  type : string,
  title : string,
  text : string,
  color : string|undefined;
  lastRating: number;
  nextRepetition: string | undefined;
  paragraph: string;
}
