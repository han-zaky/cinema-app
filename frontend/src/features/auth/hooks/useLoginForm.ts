import React, { useState } from "react";
import { login } from "../api/client";
import { useAuth } from "../components/AuthContext";

interface UseLoginFormReturn {
  email: string;
  setEmail: (value: string) => void;
  password: string;
  setPassword: (value: string) => void;
  error: string | null;
  isLoading: boolean;
  handleSubmit: (e: React.SubmitEvent<HTMLFormElement>) => Promise<void>;
}

const { setUser } = useAuth();

export function useLoginForm(): UseLoginFormReturn {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    if (!email || !password) {
      setError("Vyplň email i heslo!");
      return;
    }

    setIsLoading(true);
    try {
      const res = await login({ email, password });
      setUser(res);
    } catch (err) {
      setError("Přihlášení selhalo!");
    } finally {
      setIsLoading(false);
    }
  };

  return {
    email,
    setEmail,
    password,
    setPassword,
    error,
    isLoading,
    handleSubmit,
  };
}
