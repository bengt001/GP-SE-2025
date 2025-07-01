import type Card from "@/types/Card";

export default interface Deck {
  title: string;
  stapel_id: number[];
  author_id: number;
  definitions:Card[];
  problems:Card[];
  schemas:Card[];
  color: string|undefined;
}
