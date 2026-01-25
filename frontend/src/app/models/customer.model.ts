export interface RegisterCustomerRequest {
  consumerId: string;
  customerName: string;
  email: string;
  mobileNumber: string;
  address?: string;
  userId: string;
  password: string;
}

export interface RegisterCustomerResponse {
  consumerId: string;
  customerName: string;
  email: string;
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
