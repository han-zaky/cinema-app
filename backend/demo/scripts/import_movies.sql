-- Seed 15 movies + genres. Safe to re-run: skips genres/movies that already exist.
-- Run:  psql -h localhost -p 5433 -U cinema -d cinemadb -f import_movies.sql
-- (or paste into any SQL client connected to cinemadb)
-- Tables must exist first (start the backend once so hibernate ddl-auto creates them).

BEGIN;

INSERT INTO genres (name) VALUES
    ('Action'), ('Adventure'), ('Animation'), ('Comedy'), ('Crime'),
    ('Drama'), ('Fantasy'), ('Horror'), ('Mystery'), ('Romance'),
    ('Sci-Fi'), ('Thriller')
ON CONFLICT (name) DO NOTHING;

CREATE TEMP TABLE seed_movies (
    title            text,
    description      text,
    duration_minutes int,
    poster_url       text,
    genres           text[]
) ON COMMIT DROP;

INSERT INTO seed_movies VALUES
('Inception', 'A thief who steals secrets through dream-sharing technology is offered a chance to have his criminal record erased by planting an idea in a target''s mind.', 148, 'https://placehold.co/300x450?text=Inception', ARRAY['Action','Sci-Fi','Thriller']),
('The Dark Knight', 'Batman faces the Joker, a criminal mastermind who plunges Gotham City into anarchy.', 152, 'https://placehold.co/300x450?text=The+Dark+Knight', ARRAY['Action','Crime','Drama']),
('Interstellar', 'A team of explorers travels through a wormhole in space in an attempt to ensure humanity''s survival.', 169, 'https://placehold.co/300x450?text=Interstellar', ARRAY['Adventure','Drama','Sci-Fi']),
('The Matrix', 'A hacker discovers the world he lives in is a simulation and joins a rebellion against its controllers.', 136, 'https://placehold.co/300x450?text=The+Matrix', ARRAY['Action','Sci-Fi']),
('Pulp Fiction', 'The lives of two hitmen, a boxer, and a pair of diner bandits intertwine in four tales of violence and redemption.', 154, 'https://placehold.co/300x450?text=Pulp+Fiction', ARRAY['Crime','Drama']),
('The Shawshank Redemption', 'Two imprisoned men bond over years, finding solace and eventual redemption through acts of common decency.', 142, 'https://placehold.co/300x450?text=Shawshank', ARRAY['Drama']),
('Spirited Away', 'A young girl wanders into a world of spirits and must work in a bathhouse to free her parents.', 125, 'https://placehold.co/300x450?text=Spirited+Away', ARRAY['Animation','Adventure','Fantasy']),
('Toy Story', 'A cowboy doll is threatened when a new spaceman action figure supplants him as top toy in a boy''s room.', 81, 'https://placehold.co/300x450?text=Toy+Story', ARRAY['Animation','Adventure','Comedy']),
('Get Out', 'A young Black man visits his white girlfriend''s family estate and uncovers a disturbing secret.', 104, 'https://placehold.co/300x450?text=Get+Out', ARRAY['Horror','Mystery','Thriller']),
('The Grand Budapest Hotel', 'A legendary concierge and his lobby boy become entangled in the theft of a priceless painting.', 99, 'https://placehold.co/300x450?text=Grand+Budapest', ARRAY['Comedy','Crime','Adventure']),
('La La Land', 'A jazz pianist and an aspiring actress fall in love while pursuing their dreams in Los Angeles.', 128, 'https://placehold.co/300x450?text=La+La+Land', ARRAY['Comedy','Drama','Romance']),
('Mad Max: Fury Road', 'In a desert wasteland, a drifter and a rebel warrior flee a tyrant in a high-speed chase.', 120, 'https://placehold.co/300x450?text=Fury+Road', ARRAY['Action','Adventure','Sci-Fi']),
('Parasite', 'A poor family schemes to become employed by a wealthy household, with unforeseen consequences.', 132, 'https://placehold.co/300x450?text=Parasite', ARRAY['Comedy','Drama','Thriller']),
('The Lord of the Rings: The Fellowship of the Ring', 'A hobbit and a fellowship set out to destroy the One Ring and save Middle-earth.', 178, 'https://placehold.co/300x450?text=Fellowship', ARRAY['Adventure','Fantasy','Action']),
('Se7en', 'Two detectives hunt a serial killer who uses the seven deadly sins as his motives.', 127, 'https://placehold.co/300x450?text=Se7en', ARRAY['Crime','Mystery','Thriller']);

-- Insert movies that don't already exist (matched by title)
INSERT INTO movies (title, description, duration_minutes, poster_url)
SELECT s.title, s.description, s.duration_minutes, s.poster_url
FROM seed_movies s
WHERE NOT EXISTS (SELECT 1 FROM movies m WHERE m.title = s.title);

-- Link genres (skips links that already exist)
INSERT INTO movie_genres (movie_id, genre_id)
SELECT m.id, g.id
FROM seed_movies s
JOIN movies m ON m.title = s.title
CROSS JOIN LATERAL unnest(s.genres) AS gname
JOIN genres g ON g.name = gname
ON CONFLICT DO NOTHING;

COMMIT;
