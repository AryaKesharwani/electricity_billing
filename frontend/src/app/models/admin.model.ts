export interface RegisterAdminRequest {
  email: string;
  password: string;
}

export interface RegisterAdminResponse {
  email: string;
  userType: string;
  status: string;
  message: string;
  registeredAt: string;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  timestamp: string;
  errors?: { [key: string]: string };
}
