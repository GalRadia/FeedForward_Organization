package com.example.feedforward_association.interfaces;

import com.example.feedforward_association.models.Order;

public interface OngoingDonationCallback {
    void onStatusClicked(Order order);
}
