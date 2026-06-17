export const mockMovies = [
  {
    id: '1',
    title: 'Inception',
    genre: ['Sci-Fi', 'Thriller'],
    rating: 8.8,
    language: 'English',
    duration: '2h 28m',
    releaseDate: '2010-07-16',
    posterUrl: 'https://image.tmdb.org/t/p/w500/9gk7adzbHmFRvEa5veVPRCgMgSR.jpg',
    description: 'A thief who steals corporate secrets through dream-sharing technology is given the task of planting an idea into the mind of a C.E.O.'
  },
  {
    id: '2',
    title: 'The Dark Knight',
    genre: ['Action', 'Crime', 'Drama'],
    rating: 9.0,
    language: 'English',
    duration: '2h 32m',
    releaseDate: '2008-07-18',
    posterUrl: 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911BTUgMe1vMSJK.jpg',
    description: 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest tests.'
  },
  {
    id: '3',
    title: 'Interstellar',
    genre: ['Sci-Fi', 'Adventure'],
    rating: 8.6,
    language: 'English',
    duration: '2h 49m',
    releaseDate: '2014-11-07',
    posterUrl: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
    description: 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity\'s survival.'
  },
  {
    id: '4',
    title: 'Parasite',
    genre: ['Thriller', 'Drama'],
    rating: 8.5,
    language: 'Korean',
    duration: '2h 12m',
    releaseDate: '2019-05-30',
    posterUrl: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
    description: 'Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.'
  },
  {
    id: '5',
    title: 'Oppenheimer',
    genre: ['Biography', 'Drama'],
    rating: 8.3,
    language: 'English',
    duration: '3h 0m',
    releaseDate: '2023-07-21',
    posterUrl: 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg',
    description: 'The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.'
  },
  {
    id: '6',
    title: 'Dune: Part Two',
    genre: ['Sci-Fi', 'Adventure'],
    rating: 8.5,
    language: 'English',
    duration: '2h 46m',
    releaseDate: '2024-03-01',
    posterUrl: 'https://image.tmdb.org/t/p/w500/8b8R8l88Qje9dn9OE8PY05Nez7.jpg',
    description: 'Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.'
  },
];

export const mockTheatres = [
  {
    id: '1',
    name: 'CineVerse IMAX',
    location: 'Downtown Central',
    screens: [
      { id: 's1', name: 'Screen 1 - IMAX', capacity: 200 },
      { id: 's2', name: 'Screen 2 - Dolby', capacity: 150 },
    ]
  },
  {
    id: '2',
    name: 'CineVerse Multiplex',
    location: 'Mall of India',
    screens: [
      { id: 's3', name: 'Screen 1', capacity: 180 },
      { id: 's4', name: 'Screen 2', capacity: 120 },
      { id: 's5', name: 'Screen 3', capacity: 100 },
    ]
  },
];

export const mockShows = [
  { id: 'sh1', movieId: '1', screenId: 's1', startTime: '10:00 AM', endTime: '12:28 PM', theatre: 'CineVerse IMAX' },
  { id: 'sh2', movieId: '1', screenId: 's3', startTime: '02:00 PM', endTime: '04:28 PM', theatre: 'CineVerse Multiplex' },
  { id: 'sh3', movieId: '2', screenId: 's2', startTime: '06:00 PM', endTime: '08:32 PM', theatre: 'CineVerse IMAX' },
  { id: 'sh4', movieId: '3', screenId: 's4', startTime: '09:00 PM', endTime: '11:49 PM', theatre: 'CineVerse Multiplex' },
];

export const generateMockSeats = (showId) => {
  const rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
  const cols = 12;
  const seats = [];

  rows.forEach((row, ri) => {
    for (let col = 1; col <= cols; col++) {
      let type = 'REGULAR';
      let price = 150;
      if (ri <= 1) { type = 'VIP'; price = 350; }
      else if (ri <= 3) { type = 'PREMIUM'; price = 250; }

      let status = 'AVAILABLE';
      // Simulate some booked/locked seats
      if ((row === 'C' && col === 5) || (row === 'D' && col === 6) || (row === 'A' && col === 3)) {
        status = 'BOOKED';
      }
      if (row === 'B' && col === 7) {
        status = 'LOCKED';
      }

      seats.push({
        id: `${showId}-${row}${col}`,
        row,
        column: col,
        type,
        price,
        status,
      });
    }
  });

  return seats;
};

export const mockUsers = {
  user: { id: 'u1', name: 'Guest', email: 'user@example.com', role: 'USER' },
  admin: { id: 'u2', name: 'Admin', email: 'admin@cineverse.com', role: 'ADMIN' },
  owner: { id: 'u3', name: 'Theatre Owner', email: 'owner@cineverse.com', role: 'THEATRE_OWNER' },
};

// Mock JWT token
export const mockToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1MSIsInJvbGUiOiJVU0VSIn0.mock_signature';
