CREATE TABLE optimization_requests
(
    id                 UUID PRIMARY KEY,
    max_margin         NUMERIC(19, 4) NOT NULL,
    total_margin_used  NUMERIC(19, 4) NOT NULL,
    total_expected_pnl NUMERIC(19, 4) NOT NULL,
    created_at         TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE TABLE trades
(
    id              UUID PRIMARY KEY,
    trade_name      VARCHAR(255)   NOT NULL,
    margin_required NUMERIC(19, 4) NOT NULL,
    expected_pnl    NUMERIC(19, 4) NOT NULL,
    selected        BOOLEAN        NOT NULL,
    request_id      UUID           NOT NULL REFERENCES optimization_requests (id)
);