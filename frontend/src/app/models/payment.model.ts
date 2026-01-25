export interface PayBillRequest {
  billId: number;
  amount: number;
  paymentMethod?: string;
  transactionReference?: string;
}

export interface PayBillResponse {
  paymentId: string;
  billId: number;
  consumerId: string;
  amountPaid: number;
  paymentStatus: string;
  message: string;
  paymentDate: string;
  transactionReference?: string;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  timestamp: string;
  errors?: { [key: string]: string };
}
