# Cardinal Components API (CCA) Example

This mod shows an example of a BlockEntity, which can store 5 ItemStacks at the same time.
This mod is **not** made by or associated with the CCA developer(s).

The Block Entity Inventory Component is synced to let the ItemStacks be rendered inside the
[BlockEntityRenderer](src/main/java/net/shirojr/ccaexample/block/client/ItemHolderBlockEntityRenderer.java)
and ticked on both logical server side (inventory management) and the client side (ItemStack hovering animation).
With CCA the [Block](./src/main/java/net/shirojr/ccaexample/block/ItemHolderBlock.java)
and [BlockEntity](./src/main/java/net/shirojr/ccaexample/block/entity/ItemHolderBlockEntity.java) classes can stay
pretty much barebones in terms of boilerplate code.

Example of the Block (Youtube):

[![BlockEntity in Action](https://img.youtube.com/vi/1xTIzWlwzIQ/0.jpg)](https://www.youtube.com/watch?v=1xTIzWlwzIQ)

## Where To Start?

Cardinal Components API is fairly simple to set up. It's [Wiki](https://ladysnake.org/wiki/cardinal-components-api/)
and the JavaDocs explain most usages pretty well. People who need an example, can take a look at the code here in this
mod.

Cardinal Components API uses a component system to add arbitrary data to a lot of different providers (e.g. Entities,
Chunks, Worlds, ...). For now, we will only use
the [Block Provider](https://ladysnake.org/wiki/cardinal-components-api/modules/block).

---

### 1. Setup

The dependencies for the CCA API are listed in the [build.gradle](./build.gradle)
and [gradle.properties](./gradle.properties) files.

You should also take a look at the [fabric.mod.json](./src/main/resources/fabric.mod.json) file. In there the
Entrypoints need to be specified and the `custom` field is defined to register all components.

- https://ladysnake.org/wiki/cardinal-components-api/dev-install
- https://ladysnake.org/wiki/cardinal-components-api/registration

---

### 2. Creating Components

To create components, simply make
a [new Interface](./src/main/java/net/shirojr/ccaexample/cca/component/SyncedBlockInventoryComponent.java) which
specifies methods to access and modify stored / shared data. Also register them in
your [CCA entrypoint class](./src/main/java/net/shirojr/ccaexample/CCAExampleComponents.java).

A personal preference is to add a utility method, which helps out with accessing the Component.

```java
static SyncedBlockInventoryComponent fromBlockEntity(ItemHolderBlockEntity blockEntity) {
    // getting the Component from the Component Entrypoint class using the ComponentKey
    return CCAExampleComponents.SYNCED_BLOCK_INVENTORY.get(blockEntity);
}
```

The Component Interfaces define, how you can interact with the Components.

If ticking is necessary, either
`ClientTickingComponent`, `ServerTickingComponent` or `CommonTickingComponent` need to be specified on this interface
to open up those for registration.

- https://ladysnake.org/wiki/cardinal-components-api/implementing-component

---

### 3. Creating Component Implementations

The [implementation class](./src/main/java/net/shirojr/ccaexample/cca/implementation/SyncedBlockEntityInventoryComponentImpl.java)
defines how the component is actually interacting with the provider, how the
ticking is resolved and how networking is handled. When interacting with Components, only the Interface from earlier, is
used. 

- https://ladysnake.org/wiki/cardinal-components-api/implementing-component
- https://ladysnake.org/wiki/cardinal-components-api/registration
