export default interface Deck {
  title: string;
  stapel_id: number[];
  author_id: number;
  cards: number[];
  color: string|undefined;
}
