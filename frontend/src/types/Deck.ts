export default interface Deck {
  title: string;
  stapel_id: number[];
  author_id: number;
  cards: number[];
  definitions:number;
  problems:number;
  schemas:number;
  color: string|undefined;
}
