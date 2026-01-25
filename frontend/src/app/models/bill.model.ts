export interface BillResponse {
  billId: number;
  consumerId: string;
  billDate: string;
  dueDate: string;
  unitsConsumed: number;
  amount: number;
  status: string;
  description?: string;
  createdAt: string;
}

export interface CreateBillRequest {
  consumerId: string;
  billDate: string;
  dueDate: string;
  unitsConsumed: number;
  amount: number;
  description?: string;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  timestamp: string;
  errors?: { [key: string]: string };
}
