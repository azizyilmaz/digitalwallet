package com.aziz.digitalwallet.persistence.adapter;

import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.persistence.entity.WalletEntity;
import com.aziz.digitalwallet.persistence.repository.SpringDataWalletRepository;
import com.aziz.digitalwallet.service.port.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryAdapter implements WalletRepository {
    
    private final SpringDataWalletRepository repo;

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = toEntity(wallet);
        WalletEntity saved = repo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Wallet> findByCustomerId(UUID customerId) {
        return repo.findByCustomerId(customerId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private WalletEntity toEntity(Wallet wallet) {
        WalletEntity e = new WalletEntity();
        e.setId(wallet.getId());
        e.setCustomerId(wallet.getCustomerId());
        e.setWalletName(wallet.getWalletName());
        e.setCurrency(wallet.getCurrency());
        e.setBalance(wallet.getBalance());
        e.setUsableBalance(wallet.getUsableBalance());
        e.setActiveForShopping(wallet.isActiveForShopping());
        e.setActiveForWithdraw(wallet.isActiveForWithdraw());
        return e;
    }

    private Wallet toDomain(WalletEntity e) {
        Wallet w = new Wallet();
        w.setId(e.getId());
        w.setCustomerId(e.getCustomerId());
        w.setWalletName(e.getWalletName());
        w.setCurrency(e.getCurrency());
        w.setBalance(e.getBalance());
        w.setUsableBalance(e.getUsableBalance());
        w.setActiveForShopping(e.isActiveForShopping());
        w.setActiveForWithdraw(e.isActiveForWithdraw());
        return w;
    }

}
