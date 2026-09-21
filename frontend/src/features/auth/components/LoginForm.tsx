import { useLoginForm } from "../hooks/useLoginForm";

export default function LoginForm() {
  const {
    email,
    setEmail,
    password,
    setPassword,
    error,
    isLoading,
    handleSubmit,
  } = useLoginForm();
  return (
    <form onSubmit={handleSubmit}>
      <h1>Login</h1>
      <label htmlFor="email">Email</label>
      <input
        type="email"
        value={email}
        onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
          setEmail(e.target.value);
        }}
        required
        placeholder="Email"
        id="email"
      />
      <label htmlFor="password">Password</label>
      <input
        type="password"
        value={password}
        onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
          setPassword(e.target.value)
        }
        required
        placeholder="Password"
        id="password"
      />
      {error && <p style={{ color: "red" }}>{error}</p>}
      <button type="submit" disabled={isLoading}>
        Login
      </button>
    </form>
  );
}
