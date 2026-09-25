import { useRegisterForm } from "../hooks/useRegisterForm";

export function RegisterForm() {
  const {
    firstName,
    setFirstName,
    lastName,
    setLastName,
    email,
    setEmail,
    password,
    setPassword,
    error,
    isLoading,
    result,
    handleSubmit,
  } = useRegisterForm();

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <h1>Register</h1>
        <label htmlFor="firstname">First name</label>
        <input
          type="text"
          value={firstName}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
            setFirstName(e.target.value);
          }}
          required
          placeholder="First name"
          id="firstname"
        />
        <label htmlFor="lastname">Last Name</label>
        <input
          type="text"
          value={lastName}
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
            setLastName(e.target.value);
          }}
          required
          placeholder="Last name"
          id="lastname"
        />
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
          Register
        </button>
      </form>

      {result && (
        <div>
          <p>First name: {result.firstName}</p>
          <p>Last name: {result.lastName}</p>
        </div>
      )}
    </div>
  );
}
