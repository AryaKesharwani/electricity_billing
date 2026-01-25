export interface LoginRequest {
  userName: string;
  password: string;
}

export interface LoginResponse {
  email: string;
  userType: string;
  status: string;
  message: string;
  loginTime: string;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  timestamp: string;
  errors?: { [key: string]: string };
}
