import { useState } from "react";

interface useRegisterFormReturn {
  firstName: string;
  setFirstName: (value: string) => void;
  lastName: string;
  setLastName: (value: string) => void;
  email: string;
  setEmail: (value: string) => void;
  password: string;
  setPassword: (value: string) => void;
  error: string | null;
  isLoading: boolean;
  handleSubmit: (e: React.SubmitEvent<HTMLFormElement>) => Promise<void>;
}

export function useRegisterForm(): useRegisterFormReturn {
  const [firstName, setFirstName] = useState<string>("");
  const [lastName, setLastName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    if (!email || !password || !firstName || !lastName) {
      setError("Všechna pole jsou povinná!");
      return;
    }

    setIsLoading(true);
    try {
      await fakeRegister();
    } catch (err) {
      setError("Registrace selhala");
      setIsLoading(false);
    }
  };

  return {
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
    handleSubmit,
  };
}

//fake register
async function fakeRegister(): Promise<void> {
  return;
}
